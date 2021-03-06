import Vue from 'vue'
export default {
  parseDataSize(size) {
    // console.log('size', size)
    // console.log('size.endsWith(GB)', size.endsWith('GB'))
    let byteSize
    if (size.endsWith('TB')) {
      byteSize = parseInt(size.slice(0, size.length - 2).trim()) * 1024 * 1024 * 1024 * 1024
    } else if (size.endsWith('GB')) {
      byteSize = parseInt(size.slice(0, size.length - 2).trim()) * 1024 * 1024 * 1024
    } else if (size.endsWith('MB')) {
      byteSize = parseInt(size.slice(0, size.length - 2).trim()) * 1024 * 1024
    } else if (size.endsWith('KB')) {
      byteSize = parseInt(size.slice(0, size.length - 2).trim()) * 1024
    } else if (size.endsWith('B')) {
      byteSize = parseInt(size.slice(0, size.length - 1).trim())
    }
    return byteSize
  }
  // humanreadableDuration: function (duration) {
  //   var humanReadable = ''
  //   if (duration > (1000 * 60 * 60 * 24)) {
  //     humanReadable += parseInt(duration / (1000 * 60 * 60 * 24)) + this.$t('msg.durationUnit.day')
  //     duration = duration % (1000 * 60 * 60 * 24)
  //   }
  //   if (duration > (1000 * 60 * 60)) {
  //     humanReadable += parseInt(duration / (1000 * 60 * 60)) + this.$t('msg.durationUnit.hour')
  //     duration = duration % (1000 * 60 * 60)
  //   }
  //   if (duration > (1000 * 60)) {
  //     humanReadable += parseInt(duration / (1000 * 60)) + this.$t('msg.durationUnit.minute')
  //     // duration = duration % (1000 * 60)
  //   }
  //   // if (duration > 1000) {
  //   //   humanReadable += parseInt(duration / 1000) + '秒'
  //   //   duration = duration % 1000
  //   // }
  //   if (humanReadable == '') {
  //     humanReadable = '1分钟内'
  //   } else {
  //     humanReadable += '后'
  //   }
  //   return humanReadable
  // },
  // getDuration: function(duration) {
  //   let humanReadable = {
  //     day: 0,
  //     hour: 0,
  //     minute: 0
  //   }
  //   if (duration > (1000 * 60 * 60 * 24)) {
  //     humanReadable.day = parseInt(duration / (1000 * 60 * 60 * 24))
  //     duration = duration % (1000 * 60 * 60 * 24)
  //   }
  //   if (duration > (1000 * 60 * 60)) {
  //     humanReadable.hour = parseInt(duration / (1000 * 60 * 60))
  //     duration = duration % (1000 * 60 * 60)
  //   }
  //   if (duration > (1000 * 60)) {
  //     humanReadable.minute = parseInt(duration / (1000 * 60))
  //   }
  //   console.log(humanReadable)
  //   return humanReadable
  // }
}