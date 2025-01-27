package com.wadoo.hyperion.server.entity.capsling;


import com.wadoo.hyperion.server.entity.HyperionMob;
import com.wadoo.hyperion.server.registry.ItemHandler;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;
import java.util.function.Predicate;

public class CapslingEntity extends HyperionMob implements GeoEntity, Bucketable {

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    public static final Predicate<ItemEntity> ALLOWED_ITEMS = (item) -> {
        return !item.hasPickUpDelay() && item.isAlive() && item.onGround();
    };

    // -- ANIMATIONS -- //
    protected static final RawAnimation IDLE = RawAnimation.begin().thenPlay("idle");
    protected static final RawAnimation IDLE_TALK = RawAnimation.begin().thenPlay("idle_talk");
    protected static final RawAnimation IDLE_EAT = RawAnimation.begin().thenPlay("idle_eat");
    protected static final RawAnimation WALK = RawAnimation.begin().thenPlay("walk");
    protected static final RawAnimation WALK_EAT = RawAnimation.begin().thenPlay("walk_eat");
    protected static final RawAnimation EAT_START = RawAnimation.begin().thenPlay("eat_start");
    protected static final RawAnimation EAT_LOOP = RawAnimation.begin().thenPlay("eat_loop");
    protected static final RawAnimation EAT_END = RawAnimation.begin().thenPlay("eat_end");
    protected static final RawAnimation DEATH = RawAnimation.begin().thenPlay("end");



    // -- DATA -- //

    private static final EntityDataAccessor<Boolean> OPEN = SynchedEntityData.defineId(CapslingEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> EAT_TIMER = SynchedEntityData.defineId(CapslingEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> EAT_COOLDOWN = SynchedEntityData.defineId(CapslingEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(CapslingEntity.class, EntityDataSerializers.BOOLEAN);
    /*
    0. Idle
    0b. Idle Eat
    0c. Walk
    0d. Walk Eat
    1. Idle Talk
    2. Eat Start
    3. Eat Loop
    4. Eat End
    5. Death
     */


    public CapslingEntity(EntityType<? extends HyperionMob> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OPEN, false);
        this.entityData.define(EAT_TIMER,0);
        this.entityData.define(EAT_COOLDOWN, 0);
        this.entityData.define(FROM_BUCKET, false);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 6, this::predicate)
                .triggerableAnim("eat_start", EAT_START)
                .triggerableAnim("eat_loop", EAT_LOOP)
                .triggerableAnim("eat_end", EAT_END));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 15.0D).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.KNOCKBACK_RESISTANCE, 0.7D);
    }


    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> state) {
        switch (getAnimation()){
            case 0:
                if (!state.isMoving()) {
                    if (!getOpen()) return state.setAndContinue(IDLE);
                    else {
                        return state.setAndContinue(IDLE_EAT);
                    }
                }
                else {
                    if (!getOpen()) return state.setAndContinue(WALK);
                    else {
                        return state.setAndContinue(WALK_EAT);
                    }
                }
            case 2:
                return state.setAndContinue(IDLE_EAT);
            default:
                return PlayState.CONTINUE;
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new CapslingEatGoal(this, 3, "eat_start", 141));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this,1.0d));
        this.goalSelector.addGoal(6, new FindMagmaCreamGoal(this));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(2, new CapslingTemptGoal(this, 1.0D, Ingredient.of(Items.MAGMA_CREAM), false));
        this.goalSelector.addGoal(6, new AvoidEntityGoal<>(this, Player.class, 6F, 1, 1.2));
        //this.goalSelector.addGoal(6, new AvoidEntityGoal<>(this, GruskEntity.class, 6F, 1, 1.2));

        //this.goalSelector.addGoal(9, new CapslingSocializeGoal(this));

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }


    @Override
    public void tick() {
        super.tick();
        if (getEatCooldown() > 0) setEatCooldown(getEatCooldown() - 1);
        if (getMainHandItem().is(Items.MAGMA_CREAM) && getAnimation() != 3) this.setAnimation(3);
        if (getEatTimer() != 0 && level().isClientSide && random.nextFloat() < 0.3f && getMainHandItem().is(Items.MAGMA_CREAM)){
            //left
            Vec3 v1 = rotateModelVecAlongYAxis(new Vec3(-4.5/16f, 8/16f, 0f));
            //right
            Vec3 v2 = rotateModelVecAlongYAxis(new Vec3(4.5/16f, 8/16f, 0f));
            this.level().addParticle(ParticleTypes.SMOKE, v1.x, v1.y, v1.z, 0f, random.nextFloat()/8f, 0f);
            this.level().addParticle(ParticleTypes.SMOKE, v2.x, v2.y, v2.z, 0f, random.nextFloat()/8f, 0f);

        }
    }


    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player.getItemInHand(hand).is(Items.MAGMA_CREAM) && this.getItemBySlot(EquipmentSlot.MAINHAND).is(Items.AIR)){
            this.setItemInHand(InteractionHand.MAIN_HAND,new ItemStack(Items.MAGMA_CREAM,1));
            player.getItemInHand(hand).shrink(1);
            return InteractionResult.CONSUME;
        }
        if (player.getItemInHand(hand).is(Items.LAVA_BUCKET)) {
            return this.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
        }
        return InteractionResult.FAIL;
    }

    public void setOpen(boolean i){
        this.entityData.set(OPEN, i);
    }

    public boolean getOpen(){
        return this.entityData.get(OPEN);
    }

    public void setEatTimer(int i){
        this.entityData.set(EAT_TIMER, i);
    }

    public int getEatTimer(){
        return this.entityData.get(EAT_TIMER);
    }

    public void setEatCooldown(int i){
        this.entityData.set(EAT_COOLDOWN, i);
    }

    public int getEatCooldown(){
        return this.entityData.get(EAT_COOLDOWN);
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.entityData.set(FROM_BUCKET,fromBucket);
    }

    @Override
    public void saveToBucketTag(ItemStack stack) {
        Bucketable.saveDefaultDataToBucketTag(this, stack);
    }

    @Override
    public void loadFromBucketTag(CompoundTag tag) {
        Bucketable.loadDefaultDataFromBucketTag(this, tag);
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(ItemHandler.CAPSLING_BUCKET.get(), 1);
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_LAVA;
        //return SoundsRegistry.CAPSLING_INTO_BUCKET.get();
    }

    static <T extends LivingEntity & Bucketable> Optional<InteractionResult> bucketMobPickup(Player p_148829_, InteractionHand p_148830_, T p_148831_) {
        ItemStack itemstack = p_148829_.getItemInHand(p_148830_);
        if (itemstack.getItem() == Items.LAVA_BUCKET && p_148831_.isAlive()) {
            p_148831_.playSound(p_148831_.getPickupSound(), 1.0F, 1.0F);
            ItemStack itemstack1 = p_148831_.getBucketItemStack();
            p_148831_.saveToBucketTag(itemstack1);
            ItemStack itemstack2 = ItemUtils.createFilledResult(itemstack, p_148829_, itemstack1, false);
            p_148829_.setItemInHand(p_148830_, itemstack2);
            Level level = p_148831_.level();
            if (!level.isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) p_148829_, itemstack1);
            }

            p_148831_.discard();
            return Optional.of(InteractionResult.sidedSuccess(level.isClientSide));
        } else {
            return Optional.empty();

        }
    }
}
