<div align="center">
    <img src="https://img.shields.io/github/issues/DLindustries/System?style=flat" alt="issues">
    <img src="https://img.shields.io/badge/license-GPLV3-green" alt="License">
    <img src="https://tokei.rs/b1/github/DLindustries/System?category=code&style=flat" alt="Lines of code">
</p>

[![Github Release Downloads](https://img.shields.io/github/downloads/DLindustries/System/total?label=Github%20Release%20Downloads&style=flat-square)](https://github.com/DLindustries/System/releases)



[![Watch the video](https://img.youtube.com/vi/59nsohCazPo/0.jpg)](https://www.youtube.com/watch?v=59nsohCazPo)


<a href="https://discord.gg/yynpznJVkC"><img src="https://invidget.switchblade.xyz/yynpznJVkC" alt="https://discord.gg/yynpznJVkC"/></a><br>

## Mod description

</div>

## 🔧 Improvements Over Original Argon Client

**ORIGINAL PROJECT**

https://github.com/LvStrnggg/argon/


> ⚠️ Note: This is not a full rewrite — it's still fundamentally Argon, but with meaningful tweaks to **bypass**, **optimize**, and **refine** both visuals and behavior for smoother gameplay under grim anticheat and vulcan.





## How to use the client

1. Download the latest version of the mod (Jar file only)
2. If you haven't already knew, you are required to download fabric api jar to be run alongside the mod and use fabric loader for 1.21.1 or 1.21 to launch it. there are youtube tutorials for this
3. move the mod jar into your minecraft mods folder.
4. launch minecraft using fabric loader 1.21.1 or 1.21
5. build a suitable configuration for the servers you are targeting to bypass 
6. Enjoy


## GUI


![gui](https://github.com/user-attachments/assets/105ce9a6-8dfd-446d-9b79-277b9ec9e015)

![Screenshot ](https://github.com/user-attachments/assets/ec2128d5-e158-4aea-93ce-b9c55f5418e0)

![Screenshot ](https://github.com/user-attachments/assets/23832c9f-33e0-438f-86b9-a5bc885b9ee2)
![Screenshot ](https://github.com/user-attachments/assets/b5747333-413d-40ad-b40c-35d272b5d9b6)


## Configs

**Grim AC** - PvpHub, Donut SMP, and likely any other large Popular servers.

DON'T USE
- Autototem-blatant
- Autocrystal to lower than 2 ticks for both break and place
- Anchor Macro set to super fast - 0 ticks - test on loyisa.cn anticheat test server
- break optimizer
- jump optimizer
- totem offhand

**Bypassable when done correctly. if you are bad at crystal pvp in the first place avoid using these modules**

- Dtapsetup 
- anchor spam along with airplacement with anchor macro and airplace optimizer


*YOU MUST*
- auto xp not 0 delay
- misclick optimizer MUST BE ENABLED WITH ALL SUBFUNCTIONS
- placement optimizer MUST EXCLUDE ANCHORS
- Use common sense, most 0 delays flag. Please check using a Anticheat server - eu.loyisa.cn

**Vulcan and other insignificant anticheats** - Eupvp.net, mcprac.net, mcpvp.club, Pvp Legacy, Stray.gg

Most modules bypasses. If you want safety, then:

*Use the same config for Grim AC BUT*

- you can use anchor macro safe and quickly using the DEFAULT settings.
- you can use Dig optimizer  - not recommended but usable
- you can use jump optimizer - not recommended but usable
- you can use dtapsetup

**This is only a guide/general idea. For your own safety, i HIGHLY recommend you test your configs on a anticheat test server. BEFORE playing any of your favourite servers.**

## License

This project is subject to the [GNU General Public License v3.0](https://www.gnu.org/licenses/gpl-3.0.en.html). This does only apply for source code located directly in this clean repository. During the development and compilation process, additional source code may be used to which we have obtained no rights. Such code is not covered by the GPL license.

For those who are unfamiliar with the license, here is a summary of its main points. This is by no means legal advice nor legally binding.

**Actions that you are allowed to do:**

- Use
- Share
- Modify

**If you do decide to use ANY code from the source:**

- **You must disclose the source code of your modified work and the source code you took from this project. This means you are not allowed to use code from this project (even partially) in a closed-source (or even obfuscated) application.**
- **Your modified application must also be licensed under the GPL.**

## Developing System

This mod uses Gradle to build the client. Install the latest version of Gradle onto your computer and install IntelliJ IDEA Ultimate, or use the free community edition.

1. Clone the repository using `git clone --recurse-submodules https://github.com/DLindustries/System`.
2. CD into the local repository with the command `cd System`.
3. Run `./gradlew genSources`.
4. Open the folder in IntelliJ — feel free to recode the client.
5. To build, simply run `./gradlew build` or create a run configuration for Gradle with the build command.
6. Enjoy
