<template>
  <div class="wbs-node" :class="`level-${piece.level}`">
    <div class="d-flex align-center">
      <div class="input-container">
        <v-text-field
          v-model="pieceName"
          variant="outlined"
          density="compact"
          bg-color="grey-lighten-2"
          class="wbs-input"
          :placeholder="placeholderText"
          @input="validateNoSpecialSymbol($event)"
          @update:model-value="updatePiece"
        ></v-text-field>
        <span v-if="fieldHadSpecialSymbol()" class="invalid-feedback">No se permite el simbolo en este campo</span>
      </div>
      <div class="ms-1 button-container">
        <v-btn
          v-if="piece.level < 2"
          icon
          size="small"
          variant="text"
          @click="$emit('add-piece', piece.id)"
        >
          <v-icon color="blue-darken-3">mdi-plus</v-icon>
        </v-btn>
        <v-btn
          v-if="piece.childId != 1"
          icon
          size="small"
          variant="text"
          color="error"
          @click="$emit('delete-piece', piece.id)"
        >
          <v-icon>mdi-minus</v-icon>
        </v-btn>
      </div>
    </div>
    <TransitionGroup name="list" tag="div" class="children-container">
      <WBSNode
        v-for="child in children"
        :key="child.id"
        :piece="child"
        :all-pieces="allPieces"
        @add-piece="$emit('add-piece', $event)"
        @delete-piece="$emit('delete-piece', $event)"
        @update-piece="handleUpdatePiece"
      />
    </TransitionGroup>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
  piece: {
    type: Object,
    required: true
  },
  allPieces: {
    type: Array,
    required: true
  }
})

const emit = defineEmits(['add-piece', 'delete-piece', 'update-piece'])

const pieceName = ref(props.piece.name)
const invalidName = ref(false)

const forbiddenCharacters = /[|{},;]/g;

const children = computed(() => {
  return props.allPieces.filter(p => p.parentId === props.piece.id)
})

function updatePiece() {
  emit('update-piece', props.piece.id, pieceName.value.replace(forbiddenCharacters, ''));
}

function handleUpdatePiece(childId, newName) {
  const child = children.value.find(c => c.id === childId);
  if (child) {
    child.name = newName; // Update the local child node's name
    emit('update-piece', childId, newName); // Propagate the change upward
  }
}

const placeholderText = computed(() => {
  if (props.piece.level === 1) {
    return 'Nombre de la Fase';
  } else if (props.piece.level === 2) {
    return 'Nombre de la Tarea';
  }
  return 'Ingrese un Nombre';
});

const hasSpecialSymbol = (text) => {
  return forbiddenCharacters.test(text);
};

const validateNoSpecialSymbol = (event) => {
    const fieldValue = event.target.value;
    if (hasSpecialSymbol(fieldValue)) {
      invalidName.value = true;
      pieceName.value = pieceName.value.replace(forbiddenCharacters, '');
    } else {
      invalidName.value = false;
    }
};

const fieldHadSpecialSymbol = () => {
  return invalidName.value;
};

</script>

<style scoped>
.wbs-node {
  margin-bottom: 1rem;
  transition: all 0.3s ease;
}

.input-container {
  display: flex;
  flex-direction: column;
  width: 350px; /* Ensure the input takes up full width */
}

.wbs-input {
  width: 350px; /* Ensure the input takes up full width */
  max-width: 350px; /* Restrict maximum width */
  margin-bottom: -10px;
}

.button-container {
  display: flex;
  position: relative;
  align-self: flex-start; /* Align buttons relative to the input */
}

.children-container {
  margin-left: 2rem;
}

.level-1 {
  margin-left: 2rem;
}

.level-2 {
  margin-left: 4rem;
}

.level-3 {
  margin-left: 6rem;
}

.list-enter-active,
.list-leave-active {
  transition: all 0.5s ease;
}

.list-enter-from,
.list-leave-to {
  opacity: 0;
  transform: translateX(30px);
}

.invalid-feedback {
  color: #ff7d8a;
  font-size: 0.875rem;
  margin-top: 0.25rem;
  margin-bottom: 1rem;
  display: block;
}
</style>


