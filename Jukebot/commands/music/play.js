const Discord = require('discord.js-commando');
const YTDL = require('ytdl-core');

class PlayMusicCommand extends Discord.Command{
    constructor(client){
        super(client,{
            name: 'play',
            group: 'music',
            memberName: 'play',
            description:'Play music from youtube'
        });
        this.setServer = function(id) {
            global.servers[id] = {
                queue: [],
                live: {},
                repeat : 0
            }
        }
        this.isServerExist = function (id) { return !(!global.servers[id]);}
        this.playMusic = function (id,connection, message) {
            if (!this.isServerExist(id)) this.setServer(id);
            if (global.servers[id].queue.length === 0) 
            { 
                return message.reply('Playlist empty');
            }

            global.servers[id].dispatcher = connection.playStream(YTDL(global.servers[id].queue[0].url, { filter: 'audioonly' }));
            global.servers[id].live = global.servers[id].queue[0];
            global.servers[id].queue.shift();
            message.say(`Now playing: ${global.servers[id].live.title}`)
            if (global.servers[id].repeat > 0) global.servers[id].repeat--;
            global.servers[id].dispatcher.on('end', () => {
                if (global.servers[id].queue[0]) {
                    this.playMusic(id, connection, message);
                }
                else{
                    connection.disconnect();
                    global.servers[id].live = new {};
                } 
            });
            global.servers[id].dispatcher.on('stop', () => {
                global.servers[id].live = new {};
                connection.disconnect();
            });
        }
    }

    async run(message, { YURL}) {
        if(!message.member.voiceChannel){
            return message.reply("You must be in a voice channel!");
        }

        if (!message.guild.voiceConnection){
            message.member.voiceChannel.join().then((connection) =>{
                this.playMusic(message.guild.id,connection, message);
            })
        }
    }
}

module.exports = PlayMusicCommand;