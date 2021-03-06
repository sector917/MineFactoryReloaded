package powercrystals.minefactoryreloaded.tile.machine;

import java.util.List;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;

import powercrystals.minefactoryreloaded.MFRRegistry;
import powercrystals.minefactoryreloaded.core.GrindingDamage;
import powercrystals.minefactoryreloaded.setup.Machine;

public class TileEntitySlaughterhouse extends TileEntityGrinder
{
	public TileEntitySlaughterhouse()
	{
		super(Machine.Slaughterhouse);
		_damageSource = new GrindingDamage("mfr.slaughterhouse", 2);
	}
	
	@Override
	public void setWorldObj(World world)
	{
		super.setWorldObj(world);
		//this._grindingWorld.setAllowSpawns(true);
	}
	
	@Override
	public String getGuiBackground()
	{
		return "slaughterhouse.png";
	}
	
	@Override
	public boolean activateMachine()
	{
		//_grindingWorld.cleanReferences();
		List<?> entities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, _areaManager.getHarvestArea().toAxisAlignedBB());
		
		entityList: for(Object o : entities)
		{
			EntityLivingBase e = (EntityLivingBase)o;
			for(Class<?> t : MFRRegistry.getSlaughterhouseBlacklist())
			{
				if(t.isInstance(e))
				{
					continue entityList;
				}
			}
			if((e instanceof EntityAgeable && ((EntityAgeable)e).getGrowingAge() < 0) || e.isEntityInvulnerable() || e.func_110143_aJ() <= 0)
					//|| !_grindingWorld.addEntityForGrinding(e))
			{
				continue;
			}
			double massFound = Math.pow(e.boundingBox.getAverageEdgeLength(), 2);
			damageEntity(e);
			if(e.func_110143_aJ() <= 0)
			{
				_tank.fill(FluidRegistry.getFluidStack(_rand.nextInt(8) == 0 ? "pinkslime" : "meat", (int)(100 * massFound)), true);
				setIdleTicks(10);
			}
			else
			{
				setIdleTicks(5);
			}
			return true;
		}
		setIdleTicks(getIdleTicksMax());
		return false;
	}
	
	@Override
	protected void damageEntity(EntityLivingBase entity)
	{
		setRecentlyHit(entity, 0);
		entity.attackEntityFrom(_damageSource, DAMAGE);
	}
	
	@Override
	public int getEnergyStoredMax()
	{
		return 16000;
	}
	
	@Override
	public boolean manageSolids()
	{
		return false;
	}
}
