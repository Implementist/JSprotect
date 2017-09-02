var babelpolyfill = require("babel-polyfill");
module.exports = {
    // configuration
    //代表入口(总)文件，可以写多个
    entry: "E:\\XAMPP\\tomcat\\webapps\\ROOT\\Temp\\app-compile.js",
    output: {
        path: "E:\\XAMPP\\tomcat\\webapps\\ROOT\\Temp", //输出文件夹
        filename: "app-webpack.js" //最终打包生成的文件名
    },
};