"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var child_process_1 = require("child_process");
var fs_1 = require("fs");
(0, child_process_1.exec)("git rev-list --count HEAD", function (err, versionCode, stderr) {
    if (err) {
        console.log(err);
        return;
    }
    var filePath = "./app/build.gradle.kts";
    try {
        var data = (0, fs_1.readFileSync)(filePath, "utf-8");
        var regex = /(?<=versionCode\s*=\s*)\d+/;
        var oldVerionCode = data.match(regex);
        var newString = data.replace(regex, versionCode.trim());
        try {
            (0, fs_1.writeFileSync)(filePath, newString);
            console.log("\u7248\u672C\u53F7\u66F4\u65B0\u5B8C\u6210\uFF0C\u5DF2\u7531 ".concat(oldVerionCode, " \u66F4\u65B0\u81F3 ").concat(versionCode));
            process.exit(0);
        }
        catch (e) {
            console.log(e);
        }
    }
    catch (e) {
        console.log(e);
    }
});
