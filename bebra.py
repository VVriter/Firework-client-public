# -*- coding: utf-8 -*-
import asyncio

import discord
from discord.ext import commands
from discord.ext.commands import Bot

intents = discord.Intents.default()

bot = Bot(command_prefix='-', intents=intents)


@bot.event
async def on_ready():
   print(f'Logged as: {bot.user}')
   while True:
       await bot.change_presence(activity=discord.Activity(type=1, name="-info"))
       await asyncio.sleep(5)

       await bot.change_presence(activity=discord.Activity(type=1, name="Firework"))
       await asyncio.sleep(5)


@bot.event
async def on_command_error(ctx, error):
    if isinstance(error, commands.CommandNotFound):
        await ctx.send("**Invalid command. Try using** `-info` **to figure out commands!**")
    if isinstance(error, commands.MissingRequiredArgument):
        await ctx.send('**Please pass in all requirements.**')
    if isinstance(error, commands.MissingPermissions):
        await ctx.send("**You dont have all the requirements or permissions for using this command :angry:**")


@bot.command()
async def info(ctx):
    spotifyEmbed = discord.Embed()
    spotifyEmbed.color = discord.Color.dark_purple()
    spotifyEmbed.set_thumbnail(url="https://cdn.discordapp.com/attachments/978142431130296370/985434306866462750/SAVE_20220612_094331.jpg")
    spotifyEmbed.add_field(name="Commands", value="-info\n-site\n-server\n-issue\n-suggest", inline=True)
    spotifyEmbed.add_field(name="Usage", value="Sends this list of commands\nSends site link\nServer info\nCreate issue for developers\nCreate new suggestion",inline=True)
    spotifyMessage = await ctx.channel.send(embed=spotifyEmbed)


@bot.listen()
async def on_message(message):
    if "hack" in message.content.lower():
        spotifyEmbed = discord.Embed(title="WARNING!", description="Please do not discuss hacks in this discord.")
        spotifyEmbed.color = discord.Color.dark_purple()
        spotifyEmbed.set_thumbnail(url="https://cdn.discordapp.com/attachments/978142431130296370/985434306866462750/SAVE_20220612_094331.jpg")
        spotifyMessage = await message.channel.send(embed=spotifyEmbed)


@bot.listen()
async def on_message(message):
    if "gui" in message.content.lower():
        spotifyEmbed = discord.Embed(title="To open ClickGui press ```R-SHIFT``` button!")
        spotifyEmbed.color = discord.Color.dark_purple()
        spotifyEmbed.set_thumbnail(url="https://cdn.discordapp.com/attachments/978142431130296370/985434306866462750/SAVE_20220612_094331.jpg")
        spotifyMessage = await message.channel.send(embed=spotifyEmbed)


@bot.listen()
async def on_message(message):
    if "hud" in message.content.lower():
        spotifyEmbed = discord.Embed(title="To open Hud editor press ```L``` button!")
        spotifyEmbed.color = discord.Color.dark_purple()
        spotifyEmbed.set_thumbnail(url="https://cdn.discordapp.com/attachments/978142431130296370/985434306866462750/SAVE_20220612_094331.jpg")
        spotifyMessage = await message.channel.send(embed=spotifyEmbed)

@bot.command()
async def server(ctx):
    embed = discord.Embed(color=discord.Color.dark_purple())
    embed.add_field(name="Server Owner", value="PunCakeCat", inline=True)
    embed.add_field(name="Server created at", value=f"{ctx.guild.created_at}", inline=True)
    embed.set_thumbnail(url="https://cdn.discordapp.com/attachments/978142431130296370/985434306866462750/SAVE_20220612_094331.jpg")
    await ctx.send(embed=embed)


@bot.command()
async def site(ctx):
    await ctx.send("https://fireworkclient.ua")


@bot.command()
async def issue(ctx, *, string: str):
    channel = bot.get_channel(1003622985271345202)
    embed = discord.Embed(color=discord.Color.dark_purple(), title=f"{ctx.message.author} opened new issue!", description=string)
    embed.set_thumbnail(
        url="https://cdn.discordapp.com/attachments/978142431130296370/985434306866462750/SAVE_20220612_094331.jpg")
    spotifyMessage = await channel.send(embed=embed)
    await spotifyMessage.add_reaction("⬆")
    await spotifyMessage.add_reaction("⬇")


@bot.command()
async def suggest(ctx, *, string: str):
    channel = bot.get_channel(982637505625071636)
    embed = discord.Embed(color=discord.Color.dark_purple(), title=f"{ctx.message.author} created new suggestion!", description=string)
    embed.set_thumbnail(
        url="https://cdn.discordapp.com/attachments/978142431130296370/985434306866462750/SAVE_20220612_094331.jpg")
    spotifyMessage = await channel.send(embed=embed)
    await ctx.message.reply("New suggestion created!")
    await spotifyMessage.add_reaction("⬆")
    await spotifyMessage.add_reaction("⬇")


bot.run("MTAwMzM2NDExMjYwNjE2NzE3MA.G5gdJe.j2mNhFL_t9Y8RIjOv1eA9zBtPvDSBHiD6fLOjo")

