var gulp = require("gulp");
var babel = require("gulp-babel");

gulp.task("default", function () {
    return gulp.src("C:\\XAMPP\\tomcat\\webapps\\ROOT\\Copy\\*.js")// ES6 源码存放的路径
        .pipe(babel({
            presets: ['es2015']
        }))
        .pipe(gulp.dest("C:\\XAMPP\\tomcat\\webapps\\ROOT\\Temp\\")); //转换成 ES5 存放的路径
});