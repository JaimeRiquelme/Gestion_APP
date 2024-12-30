<template>
  <div v-if="show" class="modal-overlay">
    <div class="modal-content">
      <h3 :class="['modal-title', `modal-title-${type}`]">{{ title }}</h3>
      <p class="modal-message">{{ message }}</p>
      <div class="modal-actions">
        <button class="confirm-button" @click="handleConfirm">
          {{ confirmText }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';

const props = defineProps({
  show: Boolean,
  title: String,
  message: String,
  type: {
    type: String,
    default: 'info', // 'info', 'error', 'warning', 'success'
  },
  confirmText: {
    type: String,
    default: 'Aceptar',
  }
});

const emit = defineEmits(['confirm']);

const handleConfirm = () => {
  emit('confirm');
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  max-width: 500px;
  width: 90%;
}

.modal-title {
  margin-top: 0;
  margin-bottom: 1rem;
  font-size: 1.5rem;
}

.modal-title-info {
  color: #00B8B0;
}

.modal-title-error {
  color: #dc3545;
}

.modal-title-warning {
  color: #ffc107;
}

.modal-title-success {
  color: #28a745;
}

.modal-message {
  color: #333;
  margin-bottom: 1.5rem;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
}

.confirm-button {
  padding: 0.75rem 1.5rem;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s;
  background-color: #00B8B0;
  color: white;
  border: none;
}

.confirm-button:hover {
  background-color: #009B94;
}
</style>
