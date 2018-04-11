const Discord = require('discord.js-commando');
const {Token} = require('./botvalues.json');
global.servers = {};

const bot = new Discord.Client(
    {
        commandPrefix: 'juke'
    }
);



bot.registry.registerGroup('music', 'Music');
bot.registry.registerDefaults();
bot.registry.registerCommandsIn(__dirname + "/commands");

bot.on('ready', () => {
    bot.user.setGame('DJ');
})

bot.login(Token);
