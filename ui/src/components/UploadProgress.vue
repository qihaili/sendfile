<template>
  <div>
    <div>
      <el-progress type="circle" :percentage="percentage" :status="status"/>
    </div>
    <div style="height: 20px; line-height: 20px">
      <span style="font-size: 14px">{{ speed }}</span>
    </div>
  </div>
</template>
<script>
export default {
  props:['percentage', 'speed'],
  data() {
    return {
    }
  },
  computed: {
    status: function() {
      return this.percentage == 100 ? 'success' : null
    }
  }
}
</script>
