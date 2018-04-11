const Discord = require('discord.js-commando');

class InfoMusicCommand extends Discord.Command {

    constructor(client) {
        super(client, {
            name: 'info',
            group: 'music',
            memberName: 'info',
            description: 'Pause the current song'
        });
    }

    async run(message, { YURL }) {
        if (!message.member.voiceChannel) {
            return message.reply("You must be in a voice channel!");
        }
        var id = message.guild.id;
        if (global.servers[id] != null)
        {
            message.say(`Now playing: ${global.servers[id].live.title}`)
            var i;
            for (i = 0; i < Math.min(global.servers[id].queue.length,5);) {
                message.say(`${++i}: ${global.servers[id].queue[i-1].title}`)
            }
        } 
    }
}

module.exports = InfoMusicCommand;