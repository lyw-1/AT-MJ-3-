Component({
  properties: {
    text: {
      type: String,
      value: ''
    },
    type: {
      type: String,
      value: 'primary'
    },
    className: {
      type: String,
      value: ''
    }
  },
  methods: {
    onTap() {
      this.triggerEvent('tap');
    }
  }
});