import { exec } from "child_process";
import { readFileSync, writeFileSync } from "fs";

exec("git rev-list --count HEAD", (err, versionCode, stderr) => {
    if (err) {
        console.log(err);
        return;
    }

    const filePath = "./app/build.gradle.kts"
    try {
        const data = readFileSync(filePath, "utf-8");
        const regex = /(?<=versionCode\s*=\s*)\d+/;
        const oldVerionCode = data.match(regex);
        const newString = data.replace(regex, versionCode.trim());
        try {
            writeFileSync(filePath, newString);
            console.log(`版本号更新完成，已由 ${oldVerionCode} 更新至 ${versionCode}`);
            process.exit(0);
        } catch (e) {
            console.log(e);
        }
    } catch (e) {
        console.log(e);
    }
});