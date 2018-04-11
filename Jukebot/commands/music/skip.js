const Discord = require('discord.js-commando');
const YTDL = require('ytdl-core');

class SkipMusicCommand extends Discord.Command {

    constructor(client) {
        super(client, {
            name: 'skip',
            group: 'music',
            memberName: 'skip',
            description: 'Skip to the next song'
        });
    }

    async run(message, { YURL }) {
        if (!message.member.voiceChannel) {
            return message.say("You must be in a voice channel!");
        }
        var id = message.guild.id;

        var i;
        for (i = 0; i < global.servers[id].repeat; i++) {
            global.servers[id].queue.shift(); 
        }
        global.servers[id].repeat = 0;
        if (global.servers[id].dispatcher) global.servers[id].dispatcher.pause();
        if (global.servers[id].dispatcher) global.servers[id].dispatcher.end();
    }
}

module.exports = SkipMusicCommand;