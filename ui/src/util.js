export default {
  humanreadableDuration: function (duration) {
    var humanReadable = ''
    if (duration > (1000 * 60 * 60 * 24)) {
      humanReadable += parseInt(duration / (1000 * 60 * 60 * 24)) + '天'
      duration = duration % (1000 * 60 * 60 * 24)
    }
    if (duration > (1000 * 60 * 60)) {
      humanReadable += parseInt(duration / (1000 * 60 * 60)) + '小时'
      duration = duration % (1000 * 60 * 60)
    }
    if (duration > (1000 * 60)) {
      humanReadable += parseInt(duration / (1000 * 60)) + '分钟'
      // duration = duration % (1000 * 60)
    }
    // if (duration > 1000) {
    //   humanReadable += parseInt(duration / 1000) + '秒'
    //   duration = duration % 1000
    // }
    if (humanReadable == '') {
      humanReadable = '1分钟内'
    } else {
      humanReadable += '后'
    }
    return humanReadable
  }
}