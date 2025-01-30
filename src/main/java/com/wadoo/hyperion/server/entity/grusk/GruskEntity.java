package com.wadoo.hyperion.server.entity.grusk;

import com.wadoo.hyperion.server.ability.AbilityType;
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
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.item.ItemEntity;
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

import java.util.function.Predicate;

public class GruskEntity extends HyperionMob implements GeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);


    // -- ANIMATIONS -- //
    protected static final RawAnimation IDLE = RawAnimation.begin().thenPlay("idle");
    protected static final RawAnimation SEARCH = RawAnimation.begin().thenPlay("search");
    protected static final RawAnimation IDLE_DEATH = RawAnimation.begin().thenPlay("idle_death");
    protected static final RawAnimation IDLE_DEATH_HEADLESS = RawAnimation.begin().thenPlay("idle_death_headless");
    protected static final RawAnimation IDLE_ROAR = RawAnimation.begin().thenPlay("idle_roar");
    protected static final RawAnimation WALK_1 = RawAnimation.begin().thenPlay("walk_1");
    protected static final RawAnimation WALK_2 = RawAnimation.begin().thenPlay("walk_2");
    protected static final RawAnimation WALK_DEATH = RawAnimation.begin().thenPlay("walk_death");
    protected static final RawAnimation WALK_DEATH_HEADLESS = RawAnimation.begin().thenPlay("walk_death_headless");


    // -- DATA -- //




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
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 6, this::predicate)
                .triggerableAnim("search", SEARCH)
                .triggerableAnim("roar", IDLE_ROAR));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 35.0D).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.KNOCKBACK_RESISTANCE, 0.2D);
    }


    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> state) {
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

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @Override
    public void tick() {
        super.tick();
    }


    @Override
    protected int getDeathDuration() {
        return 40;
    }
}
