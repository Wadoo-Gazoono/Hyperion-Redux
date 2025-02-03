package com.wadoo.hyperion.server.entity.grusk;

import com.wadoo.hyperion.server.entity.HyperionMob;
import com.wadoo.hyperion.server.entity.capsling.CapslingEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class GruskEntity extends HyperionMob implements GeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);


    // -- ANIMATIONS -- //
    protected static final RawAnimation IDLE = RawAnimation.begin().thenPlay("idle");
    protected static final RawAnimation SEARCH = RawAnimation.begin().thenPlay("search");
    protected static final RawAnimation PERK_UP = RawAnimation.begin().thenPlay("perk_up");

    protected static final RawAnimation IDLE_DEATH = RawAnimation.begin().thenPlay("idle_death");
    protected static final RawAnimation IDLE_DEATH_HEADLESS = RawAnimation.begin().thenPlay("idle_death_headless");
    protected static final RawAnimation IDLE_ROAR = RawAnimation.begin().thenPlay("idle_roar");
    protected static final RawAnimation WALK_1 = RawAnimation.begin().thenPlay("walk_1");
    protected static final RawAnimation WALK_2 = RawAnimation.begin().thenPlay("walk_2");
    protected static final RawAnimation WALK_DEATH = RawAnimation.begin().thenPlay("walk_death");
    protected static final RawAnimation WALK_DEATH_HEADLESS = RawAnimation.begin().thenPlay("walk_death_headless");


    // -- DATA -- //

    private static final EntityDataAccessor<Integer> ROAR_COOLDOWN = SynchedEntityData.defineId(GruskEntity.class, EntityDataSerializers.INT);


    public int doingNothingTicks = 0;

    public GruskEntity(EntityType<? extends HyperionMob> type, Level level) {
        super(type, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.LAVA, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0F);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ROAR_COOLDOWN, 0);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 6, this::predicate)
                .triggerableAnim("search", SEARCH)
                .triggerableAnim("perk_up", PERK_UP)
                .triggerableAnim("roar", IDLE_ROAR)
                .triggerableAnim("idle_death", IDLE_DEATH)
                .triggerableAnim("walk_death", WALK_DEATH));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 35.0D).add(Attributes.ATTACK_DAMAGE, 5.0D).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.KNOCKBACK_RESISTANCE, 0.8D);
    }


    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> state) {
        if (this.isDeadOrDying()) return PlayState.CONTINUE;
        if (state.isMoving()){
            return state.setAndContinue(WALK_1);
        }
        else{
            return state.setAndContinue(IDLE);
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this,1.0d));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 30.0F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, LivingEntity.class, 30.0F));
        this.goalSelector.addGoal(6, new GruskSpecialIdleGoal(this, 1, "search", 57));
        this.goalSelector.addGoal(6, new GruskSpecialIdleGoal(this, 2, "perk_up", 54));
        this.goalSelector.addGoal(3, new GruskRoarGoal(this, 3, "roar", 51));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, GruskEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, WitherSkeleton.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, CapslingEntity.class, true));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.1, false));

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isDeadOrDying() && !level().isClientSide()){

            this.freeze(false);
            triggerAnim("controller", "idle_death");
        }

        if(this.getRoarCD() >0)this.setRoarCD(this.getRoarCD()-1);

        if (getAnimation() == 0)doingNothingTicks++;
        else{doingNothingTicks=0;}

        if(doingNothingTicks > 100 && getRandom().nextFloat() < 0.05f && getAnimation() == 0 && !isDisturbed()){
            setAnimation(1);
        }
        if(doingNothingTicks > 100 && getRandom().nextFloat() < 0.05f && getAnimation() == 0 && !isDisturbed()){
            setAnimation(2);
        }

        if (this.getTarget() != null){
            LivingEntity target = this.getTarget();
            if(this.distanceTo(target) > 2d && getRoarCD() == 0 && getAnimation() == 0){
                if(random.nextFloat() < 0.3f) setAnimation(3);
                else{setRoarCD(random.nextInt(10,40));}
            }

        }
    }


    @Override
    protected int getDeathDuration() {
        return 60;
    }

    public boolean isDisturbed(){
        return this.getTarget() != null;
    }

    public void setRoarCD(int i){this.entityData.set(ROAR_COOLDOWN, i);}

    public int getRoarCD(){return this.entityData.get(ROAR_COOLDOWN);}
}
