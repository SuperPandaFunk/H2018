const Discord = require('discord.js-commando');
const fetchVideoInfo = require('youtube-info');

class AddMusicCommand extends Discord.Command {
    constructor(client) {
        super(client, {
            name: 'add',
            group: 'music',
            memberName: 'add',
            description: 'Play music from youtube',
            args: [
                {
                    key: 'YURL',
                    prompt: 'What is the Youtube URL?',
                    type: 'string'
                }
            ]
        });
        this.setServer = function (id) {
            global.servers[id] = {
                queue: [],
                live: {},
                repeat: 0
            }
        }
        this.isServerExist = function (id) { return !(!global.servers[id]); }
        this.addMusic = function (id, videoInfo) 
        {
            if (!this.isServerExist(id)) this.setServer(id);
            global.servers[id].queue.push(videoInfo);
        }
    }

    async run(message, { YURL }) {
        let YTID = YURL.split("watch?v=").pop();
        message.delete();
        fetchVideoInfo(YTID).then(function (videoInfo) {

            if(videoInfo.title != null)
            {
                if (global.servers[message.guild.id] == null)
                {
                    global.servers[message.guild.id] = {
                        queue: [],
                        live: {}
                    }
                }
                global.servers[message.guild.id].queue.push({ title: videoInfo.title, duration: videoInfo.duration, url: videoInfo.url });
                
                message.reply(`${videoInfo.title} as been added`);
            }else{
                message.reply(`${YURL} is not a valid youtube URL`);
            }
                

        });
    }
}

module.exports = AddMusicCommand;