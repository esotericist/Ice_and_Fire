package com.github.alexthe666.iceandfire.structures;

import com.github.alexthe666.iceandfire.block.BlockGoldPile;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import net.minecraft.block.BlockChest;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenIceDragonRoosts extends WorldGenerator {

    public static void burnGround(World world, Random rand, BlockPos position, int radius) {
        for (int i = 0; radius >= 0 && i < 3; ++i) {
            int j = radius + rand.nextInt(2);
            int k = (radius + rand.nextInt(2));
            int l = radius + rand.nextInt(2);
            float f = (float) (j + k + l) * 0.333F + 0.5F;
            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
                if (blockpos.distanceSq(position) <= (double) (f * f)) {
                    IBlockState state = world.getBlockState(blockpos);
                    if (state.getMaterial() == Material.GRASS || state.getMaterial() == Material.CRAFTED_SNOW && world.canBlockSeeSky(blockpos)) {
                        world.setBlockState(blockpos, ModBlocks.frozenGrass.getDefaultState());
                    } else if (state.getMaterial() == Material.GROUND && state.getBlock() == Blocks.DIRT || state.getMaterial() == Material.CRAFTED_SNOW && !world.canBlockSeeSky(blockpos)) {
                        world.setBlockState(blockpos, ModBlocks.frozenDirt.getDefaultState());
                    } else if (state.getMaterial() == Material.GROUND && state.getBlock() == Blocks.GRAVEL) {
                        world.setBlockState(blockpos, ModBlocks.frozenGravel.getDefaultState());
                    } else if (state.getMaterial() == Material.ROCK && (state.getBlock() == Blocks.COBBLESTONE || state.getBlock().getUnlocalizedName().contains("cobblestone"))) {
                        world.setBlockState(blockpos, ModBlocks.frozenCobblestone.getDefaultState());
                    } else if (state.getMaterial() == Material.ROCK) {
                        world.setBlockState(blockpos, ModBlocks.frozenStone.getDefaultState());
                    } else if (state.getBlock() == Blocks.GRASS_PATH) {
                        world.setBlockState(blockpos, ModBlocks.frozenGrassPath.getDefaultState());
                    } else if (state.getMaterial() == Material.WOOD) {
                        world.setBlockState(blockpos, ModBlocks.ash.getDefaultState());
                    } else if (state.getMaterial() == Material.LEAVES || state.getMaterial() == Material.PLANTS || state.getBlock() == Blocks.SNOW_LAYER) {
                        world.setBlockState(blockpos, Blocks.AIR.getDefaultState());
                    }
                }
            }
        }
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        int dragonAge = 50 + rand.nextInt(25);
        burnGround(worldIn, rand, position, dragonAge / 5);
        generateStructures(worldIn, rand, position, dragonAge / 5);
        EntityIceDragon dragon = new EntityIceDragon(worldIn);
        dragon.setGender(dragon.getRNG().nextBoolean());
        dragon.growDragon(dragonAge);
        dragon.setHealth(dragon.getMaxHealth());
        dragon.setVariant(new Random().nextInt(4));
        dragon.setPositionAndRotation(position.getX() + 0.5, worldIn.getHeight(position).getY() + 1.5, position.getZ() + 0.5, rand.nextFloat() * 360, 0);
        dragon.homeArea = position;
        dragon.setHunger(50);
        worldIn.spawnEntity(dragon);
        return true;
    }

    public void generateStructures(World world, Random rand, BlockPos position, int radius) {
        for (int i = 0; radius >= 0 && i < 3; ++i) {
            int j = radius + rand.nextInt(2);
            int k = (radius + rand.nextInt(2));
            int l = radius + rand.nextInt(2);
            float f = (float) (j + k + l) * 0.333F + 0.5F;
            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
                if (blockpos.distanceSq(position) <= (double) (f * f) && world.isAirBlock(blockpos) && world.getBlockState(blockpos.down()).getBlock().getUnlocalizedName().contains("frozen")) {
                    int chance = rand.nextInt(100);
                    if (chance < 4) {
                        int chance2 = rand.nextInt(20);
                        switch (chance2) {
                            default:
                                generateGoldPile(world, rand, blockpos);
                                break;
                            case 1:
                                generateArchNS(world, rand, blockpos);
                                break;
                            case 2:
                                generateArchEW(world, rand, blockpos);
                                break;
                        }

                    }
                }
            }
        }
    }

    public void generateGoldPile(World world, Random rand, BlockPos position) {
        int height = 1 + new Random().nextInt(7);
        int chance = rand.nextInt(100);
        if (chance < 20) {
            world.setBlockState(position, Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.HORIZONTALS[new Random().nextInt(3)]), 3);
            if (world.getBlockState(position).getBlock() instanceof BlockChest) {
                TileEntity tileentity1 = world.getTileEntity(position);
                if (tileentity1 instanceof TileEntityChest && !((TileEntityChest) tileentity1).isInvalid()) {
                    ((TileEntityChest) tileentity1).setLootTable(WorldGenIceDragonCave.ICEDRAGON_CHEST, new Random().nextLong());
                }
            }
        } else {
            world.setBlockState(position, ModBlocks.silverPile.getDefaultState().withProperty(BlockGoldPile.LAYERS, height), 3);
        }

    }

    public void generateArchNS(World world, Random rand, BlockPos position) {
        int height = 3 + rand.nextInt(1);
        int width = 1 + rand.nextInt(2);
        for (int sides = 0; sides < height; sides++) {
            world.setBlockState(position.up(sides).east(width / 2), ModBlocks.frozenCobblestone.getDefaultState(), 3);
            world.setBlockState(position.up(sides).west(width / 2), ModBlocks.frozenCobblestone.getDefaultState(), 3);
        }
        for (int way = -1; way < width; way++) {
            world.setBlockState(position.up(height).east(way), ModBlocks.frozenCobblestone.getDefaultState(), 3);
        }
    }

    public void generateArchEW(World world, Random rand, BlockPos position) {
        int height = 3 + rand.nextInt(1);
        int width = 1 + rand.nextInt(2);
        for (int sides = 0; sides < height; sides++) {
            world.setBlockState(position.up(sides).north(width / 2), ModBlocks.frozenCobblestone.getDefaultState(), 3);
            world.setBlockState(position.up(sides).south(width / 2), ModBlocks.frozenCobblestone.getDefaultState(), 3);
        }
        for (int way = 0; way < width; way++) {
            world.setBlockState(position.up(height).south(way), ModBlocks.frozenCobblestone.getDefaultState(), 3);
        }
    }
}
