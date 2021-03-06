package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModSounds;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.*;

public class ItemDragonFlute extends Item {

    public ItemDragonFlute() {
        this.maxStackSize = 1;
        this.setUnlocalizedName("iceandfire.dragon_flute");
        this.setRegistryName(IceAndFire.MODID, "dragon_flute");
        this.setCreativeTab(IceAndFire.TAB);
        GameRegistry.register(this);
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand) {
        ItemStack itemStackIn = player.getHeldItem(hand);

        float chunksize = 16 * IceAndFire.CONFIG.dragonFluteDistance;
        List<Entity> list = worldIn.<Entity>getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().expand(chunksize, 256, chunksize));
        Collections.sort(list, new Sorter(player));
        List<EntityDragonBase> dragons = new ArrayList<EntityDragonBase>();
        Iterator<Entity> itr_entities = list.iterator();
        while (itr_entities.hasNext()) {
            Entity entity = itr_entities.next();
            if(entity instanceof EntityDragonBase){
                dragons.add((EntityDragonBase)entity);
            }
        }

        Iterator<EntityDragonBase> itr_dragons = dragons.iterator();
        while (itr_dragons.hasNext()) {
            EntityDragonBase dragon = itr_dragons.next();
            if(dragon.isTamed() && dragon.isOwner(player)) {
                if (dragon.isFlying() || dragon.isHovering()) {
                    dragon.setFlying(false);
                    dragon.setHovering(false);
                }
            }
        }
        worldIn.playSound(player, player.getPosition(), ModSounds.dragonflute, SoundCategory.NEUTRAL, 3, 0.75F);

        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
    }

    public static class Sorter implements Comparator<Entity>
    {
        private final Entity theEntity;

        public Sorter(Entity theEntityIn)
        {
            this.theEntity = theEntityIn;
        }

        public int compare(Entity p_compare_1_, Entity p_compare_2_)
        {
            double d0 = this.theEntity.getDistanceSqToEntity(p_compare_1_);
            double d1 = this.theEntity.getDistanceSqToEntity(p_compare_2_);
            return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
        }
    }
}