package com.starmediadev.plugins.starmcutils.region;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * This represents a cuboid with a min corner and a max corner in a world
 */
public class Cuboid {
    
    protected String worldName;
    protected World world;
    protected int xMin, yMin, zMin, xMax, yMax, zMax;
    protected double xMinCentered, yMinCentered, zMinCentered, xMaxCentered, yMaxCentered, zMaxCentered;
    
    public Cuboid(Location pos1, Location pos2) {
        setBounds(pos1, pos2);
    }
    
    /**
     * Sets the bounds of this cuboid
     * It does nto matter on the order, this method will detect which one is the min and which one is the max
     *
     * @param pos1 The first position
     * @param pos2 The second position
     */
    public void setBounds(Location pos1, Location pos2) {
        this.worldName = pos1.getWorld().getName();
        this.xMin = Math.min(pos1.getBlockX(), pos2.getBlockX());
        this.yMin = Math.min(pos1.getBlockY(), pos2.getBlockY());
        this.zMin = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        this.xMax = Math.max(pos1.getBlockX(), pos2.getBlockX());
        this.yMax = Math.max(pos1.getBlockY(), pos2.getBlockY());
        this.zMax = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
        this.xMinCentered = this.xMin + 0.5;
        this.xMaxCentered = this.xMax + 0.5;
        this.yMinCentered = this.yMin + 0.5;
        this.yMaxCentered = this.yMax + 0.5;
        this.zMinCentered = this.zMin + 0.5;
        this.zMaxCentered = this.zMax + 0.5;
    }
    
    public Cuboid() {
    }
    
    /**
     * Gets all blocks within this cuboid
     *
     * @return The blocks
     */
    public Iterator<Block> getBlockList() {
        return getBlockList(false);
    }
    
    /**
     * Gets all blocks within this cuboid with a flag to remove air
     *
     * @param removeAir If air should be counted in the block list
     * @return The list of blocks
     */
    public Iterator<Block> getBlockList(boolean removeAir) {
        final List<Block> bL = new ArrayList<>(this.getTotalBlockSize());
        for (int x = this.xMin; x <= this.xMax; ++x) {
            for (int y = this.yMin; y <= this.yMax; ++y) {
                for (int z = this.zMin; z <= this.zMax; ++z) {
                    Block b = getWorld().getBlockAt(x, y, z);
                    if (b.getType().equals(Material.AIR)) {
                        if (removeAir) {
                            continue;
                        }
                    }
                    
                    bL.add(b);
                }
            }
        }
        return bL.iterator();
    }
    
    /**
     * Gets the total area size represented by this cuboid
     *
     * @return The total size
     */
    public int getTotalBlockSize() {
        return this.getHeight() * this.getXWidth() * this.getZWidth();
    }
    
    /**
     * Gets the world that this cuboid exists in
     *
     * @return The world
     */
    public World getWorld() {
        if (this.world == null) {
            this.world = Bukkit.getWorld(this.worldName);
        }
        return world;
    }
    
    /**
     * Gets the height of this cuboid
     *
     * @return The height
     */
    public int getHeight() {
        return this.yMax - this.yMin + 1;
    }
    
    /**
     * Gets the x width of this cuboid
     *
     * @return The x width
     */
    public int getXWidth() {
        return this.xMax - this.xMin + 1;
    }
    
    /**
     * Gets the z width of this cuboid
     *
     * @return The z width
     */
    public int getZWidth() {
        return this.zMax - this.zMin + 1;
    }
    
    /**
     * Returns if this cuboid contains an entity
     *
     * @param entity The entity
     * @return If the entity exists within this cuboid
     */
    public boolean contains(Entity entity) {
        return contains(entity.getLocation());
    }
    
    /**
     * Checks to see if this cuboid contains a location
     *
     * @param loc The location
     * @return If this cuboid contains the location
     */
    public boolean contains(Location loc) {
        if (loc == null) {
            return false;
        }
    
        int x = loc.getBlockX(), y = loc.getBlockY(), z = loc.getBlockZ();
        boolean worldEquals = loc.getWorld().getName().equalsIgnoreCase(worldName);
        boolean xEquals = x >= this.xMin && x <= this.xMax;
        boolean yEquals = y >= this.yMin && y <= this.yMax;
        boolean zEquals = z >= this.zMin && z <= this.zMax;
        return worldEquals && xEquals && yEquals && zEquals;
    }
    
    /**
     * Gets the center of this cuboid as a Bukkit Location
     *
     * @return The center
     */
    public Location getCenter() {
        return new Location(getWorld(), (this.xMax - this.xMin) / 2.0 + this.xMin, (this.yMax - this.yMin) / 2.0 + this.yMin, (this.zMax - this.zMin) / 2.0 + this.zMin);
    }
    
    /**
     * Gets the distance of this cuboid
     *
     * @return The distance
     */
    public double getDistance() {
        return this.getMinimum().distance(this.getMaximum());
    }
    
    /**
     * Gets the minimum corner as a Location
     *
     * @return The Minimum corner
     */
    public Location getMinimum() {
        return new Location(getWorld(), this.xMin, this.yMin, this.zMin);
    }
    
    /**
     * Gets the maximum corner as a Location
     *
     * @return The Maximum corner
     */
    public Location getMaximum() {
        return new Location(getWorld(), this.xMax, this.yMax, this.zMax);
    }
    
    public double getDistanceSquared() {
        return this.getMinimum().distanceSquared(this.getMaximum());
    }
    
    /**
     * Gets a random location within this cuboid
     *
     * @return The random location
     */
    public Location getRandomLocation() {
        final Random rand = new Random();
        final int x = rand.nextInt(Math.abs(this.xMax - this.xMin) + 1) + this.xMin;
        final int y = rand.nextInt(Math.abs(this.yMax - this.yMin) + 1) + this.yMin;
        final int z = rand.nextInt(Math.abs(this.zMax - this.zMin) + 1) + this.zMin;
        return new Location(getWorld(), x, y, z);
    }

//    public boolean contains(Location loc) {
//        return loc.toVector().isInAABB(new Location(this.world, xMin, yMin, zMin).toVector(), new Location(this.world, xMax, yMax, zMax).toVector());
//    }
    
    /**
     * Checks to see if this cuboid contains the coordinates
     *
     * @param world The world
     * @param x     The x
     * @param y     The y
     * @param z     The z
     * @return If it contains this
     */
    public boolean contains(World world, int x, int y, int z) {
        return (world == getWorld() && x >= this.xMin && x <= this.xMax && y >= this.yMin && y <= this.yMax && z >= this.zMin
                && z <= this.zMax);
    }
    
    /**
     * Checks to see if this cuboid contains a player
     *
     * @param player The player
     * @return If the player is within this cuboid
     */
    public boolean contains(Player player) {
        return this.contains(player.getLocation());
    }
    
    /**
     * Checks to see if this cuboid contains a location, with a margine
     *
     * @param loc   The location
     * @param marge The margine
     * @return If it does contain the location
     */
    public boolean contains(Location loc, double marge) {
        return loc.getWorld() == getWorld() && loc.getX() >= this.xMinCentered - marge && loc.getX() <= this.xMaxCentered + marge && loc.getY() >= this.yMinCentered - marge && loc
                .getY() <= this.yMaxCentered + marge && loc.getZ() >= this.zMinCentered - marge && loc.getZ() <= this.zMaxCentered + marge;
    }
    
    public int hashCode() {
        return Objects.hash(xMin, yMin, zMin, xMax, yMax, zMax);
    }
    
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cuboid cuboid = (Cuboid) o;
        return xMin == cuboid.xMin && yMin == cuboid.yMin && zMin == cuboid.zMin && xMax == cuboid.xMax && yMax == cuboid.yMax && zMax == cuboid.zMax;
    }
    
    
}
