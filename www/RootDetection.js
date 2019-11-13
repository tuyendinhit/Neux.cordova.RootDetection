var exec = require('cordova/exec');
var detect = function (arg0, success, error) {
    console.warn('root detection!', arg0, success, error);
    exec(success, error, 'RootDetection', 'detect', [arg0]);
};

module.exports = {
    detect
};

