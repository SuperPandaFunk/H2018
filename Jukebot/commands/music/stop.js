const Discord = require('discord.js-commando');

class StopMusicCommand extends Discord.Command {

    constructor(client) {
        super(client, {
            name: 'stop',
            group: 'music',
            memberName: 'stop',
            description: 'Pause the current song'
        });
    }

    async run(message, { YURL }) {
        if (!message.member.voiceChannel) {
            return message.say("You must be in a voice channel!");
        }
        var id = message.guild.id;
        if (global.servers[id].dispatcher) global.servers[id].dispatcher.stop();
        global.servers[id].live = new {};
    }
}

module.exports = StopMusicCommand;