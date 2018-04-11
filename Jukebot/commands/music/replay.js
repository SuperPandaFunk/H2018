const Discord = require('discord.js-commando');

class ReaplayMusicCommand extends Discord.Command {
    constructor(client) {
        super(client, {
            name: 'replay',
            group: 'music',
            memberName: 'replay',
            description: 'Replay the current song x time',
            args: [
                {
                    key: 'NbReplay',
                    prompt: 'How many time you want to replay the song',
                    type: 'integer',
                    default: 1
                }
            ]
        });
    }

    async run(message, { NbReplay }) {

        if (global.servers[message.guild.id] != null) {
           if (global.servers[message.guild.id].live != undefined)
           {
            var i;
            for (i = 0; i < NbReplay; i++) {
                global.servers[message.guild.id].queue.unshift(global.servers[message.guild.id].live);
            }
            global.servers[message.guild.id].repeat = NbReplay;
            message.reply(`${global.servers[message.guild.id].live.title} as been repeated ${NbReplay} time(s)`);
           }else
           {
               message.reply(`no song is currently playing`);
           }
        } else {
            message.reply(`no song is currently playing`);
        }
        

    }

    
}

module.exports = ReaplayMusicCommand;