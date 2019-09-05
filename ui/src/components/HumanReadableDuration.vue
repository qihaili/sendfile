<template>
  <span v-if="this.ttl">{{$t('msg.expireTip.normal', {ttl: (duration.day == 0 ? '' : duration.day + $t('msg.durationUnit.day')) + (duration.hour == 0 ? '' : duration.hour + $t('msg.durationUnit.hour')) + duration.minute + $t('msg.durationUnit.minute')})}}</span>
  <span v-else>{{$t('msg.expireTip.neverExpire')}}</span>
</template>
<script>
export default {
  props: {
    ttl: {
      type: Number,
    }
  },
  computed: {
    duration() {
      return this.getDuration(this.ttl)
    }
  },
  methods: {
    getDuration: function(duration) {
      let humanReadable = {
        day: 0,
        hour: 0,
        minute: 0
      }
      if (duration > (1000 * 60 * 60 * 24)) {
        humanReadable.day = parseInt(duration / (1000 * 60 * 60 * 24))
        duration = duration % (1000 * 60 * 60 * 24)
      }
      if (duration > (1000 * 60 * 60)) {
        humanReadable.hour = parseInt(duration / (1000 * 60 * 60))
        duration = duration % (1000 * 60 * 60)
      }
      if (duration > (1000 * 60)) {
        humanReadable.minute = parseInt(duration / (1000 * 60))
      }
      return humanReadable
    }
  }
}
</script>