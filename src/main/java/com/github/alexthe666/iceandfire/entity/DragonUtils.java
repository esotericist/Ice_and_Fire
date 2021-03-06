package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class DragonUtils {

    public static BlockPos getBlockInView(EntityDragonBase dragon){
        float radius = 0.75F * (0.7F * dragon.getRenderSize() / 3) * -3 - dragon.getRNG().nextInt(dragon.getDragonStage() * 6);
        float neg = dragon.getRNG().nextBoolean() ? 1 : -1;
        float angle = (0.01745329251F * dragon.renderYawOffset) + 3.15F + (dragon.getRNG().nextFloat() * neg);
        double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
        double extraZ = (double) (radius * MathHelper.cos(angle));
        BlockPos radialPos = new BlockPos(dragon.posX + extraX, 0, dragon.posZ + extraZ);
        BlockPos ground = dragon.world.getHeight(radialPos);
        int distFromGround = (int)dragon.posY - ground.getY();
        BlockPos newPos = radialPos.up(distFromGround > 16 ? (int)Math.min(IceAndFire.CONFIG.maxDragonFlight, dragon.posY + dragon.getRNG().nextInt(16) - 8) : (int)dragon.posY + dragon.getRNG().nextInt(16) + 1);
        BlockPos pos = dragon.doesWantToLand() ? ground : newPos;
        if(!dragon.isTargetBlocked(new Vec3d(newPos)) && dragon.getDistanceSqToCenter(newPos) > 6){
            return newPos;
        }
        return null;
    }

    public static BlockPos getBlockInViewHippogryph(EntityHippogryph hippo){
        float radius = 0.75F * (0.7F * 2) * -3 - hippo.getRNG().nextInt(2 * 6);
        float neg = hippo.getRNG().nextBoolean() ? 1 : -1;
        float angle = (0.01745329251F * hippo.renderYawOffset) + 3.15F + (hippo.getRNG().nextFloat() * neg);
        double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
        double extraZ = (double) (radius * MathHelper.cos(angle));
        BlockPos radialPos = new BlockPos(hippo.posX + extraX, 0, hippo.posZ + extraZ);
        BlockPos ground = hippo.world.getHeight(radialPos);
        int distFromGround = (int)hippo.posY - ground.getY();
        BlockPos newPos = radialPos.up(distFromGround > 16 ? (int)Math.min(IceAndFire.CONFIG.maxDragonFlight, hippo.posY + hippo.getRNG().nextInt(16) - 8) : (int)hippo.posY + hippo.getRNG().nextInt(16) + 1);
        BlockPos pos = hippo.doesWantToLand() ? ground : newPos;
        if(!hippo.isTargetBlocked(new Vec3d(newPos)) && hippo.getDistanceSqToCenter(newPos) > 6){
            return newPos;
        }
        return null;
    }
}
