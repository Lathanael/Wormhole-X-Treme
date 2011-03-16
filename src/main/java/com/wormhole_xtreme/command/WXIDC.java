/**
 * 
 */
package com.wormhole_xtreme.command;

import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wormhole_xtreme.WormholeXTreme;
import com.wormhole_xtreme.WormholeXTremeCommand;
import com.wormhole_xtreme.config.ConfigManager;
import com.wormhole_xtreme.config.ConfigManager.StringTypes;
import com.wormhole_xtreme.model.Stargate;
import com.wormhole_xtreme.model.StargateManager;

/**
 * @author alron
 *
 */
public class WXIDC implements CommandExecutor {

    /**
     * @param wormholeXTreme
     */
    public WXIDC(WormholeXTreme wormholeXTreme) {
        // TODO Auto-generated constructor stub
    }


    /* (non-Javadoc)
     * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        args = WormholeXTremeCommand.commandEscaper(args);  
        Player p = null;
        boolean allowed = false;
        
        if ( WormholeXTremeCommand.playerCheck(sender) )
        {
            p = (Player) sender;
        }
        
        
        if ( args.length >= 1 )
        {
            Stargate s = StargateManager.GetStargate(args[0]);
            if ( s != null )
            {
                // 1. check for permission (config, owner, or OP)
                
                if ( WormholeXTremeCommand.playerCheck(sender))
                {
                    if ( p.isOp() || 
                        (WormholeXTreme.Permissions != null && (WormholeXTreme.Permissions.has(p, "wormhole.config"))) ||
                        s.Owner.equals(p.getName()) )   
                    {
                        allowed = true;
                    }
                }
    
    
                if ( allowed || !WormholeXTremeCommand.playerCheck(sender) )
                {
                    // 2. if args other than name - do a set                
                    if ( args.length >= 2 )
                    {
                        if ( args[1].equals("-clear") )
                        {
                            // Remove from big list of all blocks
                            StargateManager.RemoveBlockIndex(s.IrisActivationBlock);
                            // Set code to "" and then remove it from stargates block list
                            s.SetIrisDeactivationCode("");
                        }
                        else
                        {
                            // Set code
                            s.SetIrisDeactivationCode(args[1]);
                            // Make sure that block is in index
                            StargateManager.AddBlockIndex(s.IrisActivationBlock, s);
                        }
                    }
                        
        
                    // 3. always display current value at end.
                    
                    String message = "IDC for gate: " + s.Name + " is:" + s.IrisDeactivationCode;
                    if ( p != null)
                    {
                        p.sendMessage(message);
                    }
                    else
                    {
                        WormholeXTreme.ThisPlugin.prettyLog(Level.INFO, false, message);
                    }
                }
                else
                {
                    p.sendMessage(ConfigManager.output_strings.get(StringTypes.PERMISSION_NO));
                }
            }
            else
            {
                String message = "Invalid Stargate: " + args[0];
                
                if ( p != null)
                {
                    p.sendMessage(message);
                }
                else
                {
                    WormholeXTreme.ThisPlugin.prettyLog(Level.INFO, false, message);
                }
            }
            return true;
        }
        return false;
    }

}