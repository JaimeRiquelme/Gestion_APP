<template>
  <div class="dashboard">
    <header class="header">
      <h1>USACH</h1>
      <p>Hola, {{ userName }}</p>
    </header>

    <main class="main-content">
      <h2>Mis proyectos</h2>
      <div class="projects-container">
        <div
          v-for="project in projects"
          :key="project.id"
          class="project-card"
        >
          <span>{{ project.id }}. {{ project.name }}</span>
          <button @click="deleteProject(project.id)" class="delete-button">
            🗑️
          </button>
        </div>

        <!-- Botón para crear nuevo proyecto -->
        <div class="project-card add-project" @click="openCreateProjectModal">
          <span>+ Crear nuevo proyecto</span>
        </div>
      </div>
    </main>

    <!-- Modal para crear proyecto -->
    <div v-if="showCreateModal" class="modal">
      <div class="modal-content">
        <h3>Crear nuevo proyecto</h3>
        <form @submit.prevent="createProject">
          <input
            v-model="newProjectName"
            type="text"
            placeholder="Nombre del proyecto"
            required
          />
          <button type="submit">Crear</button>
          <button type="button" @click="closeCreateProjectModal">Cancelar</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useCookie } from 'nuxt/app';

// Estados locales
const projects = ref([]);
const userName = ref('usuario'); // Nombre del usuario
const showCreateModal = ref(false); // Control del modal
const newProjectName = ref(''); // Nombre del proyecto a crear

const tokenCookie = useCookie('authToken'); // Token de autenticación

// Cargar proyectos al montar el componente
const fetchProjects = async () => {
  try {
    const response = await fetch('http://localhost:8080/api/v1/proyect/getAll', {
      headers: {
        Authorization: `Bearer ${tokenCookie.value}`,
      },
    });
    if (!response.ok) throw new Error('Error al cargar proyectos');
    const data = await response.json();
    projects.value = data;
  } catch (error) {
    console.error(error.message);
  }
};

// Crear un nuevo proyecto
const createProject = async () => {
  try {
    const response = await fetch('http://localhost:8080/api/v1/proyect/create', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${tokenCookie.value}`,
      },
      body: JSON.stringify({ name: newProjectName.value }),
    });
    if (!response.ok) throw new Error('Error al crear el proyecto');
    await fetchProjects();
    closeCreateProjectModal();
  } catch (error) {
    console.error(error.message);
  }
};

// Eliminar un proyecto
const deleteProject = async (id) => {
  try {
    const response = await fetch(`http://localhost:8080/api/v1/proyect/delete/${id}`, {
      method: 'DELETE',
      headers: {
        Authorization: `Bearer ${tokenCookie.value}`,
      },
    });
    if (!response.ok) throw new Error('Error al eliminar el proyecto');
    await fetchProjects();
  } catch (error) {
    console.error(error.message);
  }
};

// Mostrar/ocultar modal
const openCreateProjectModal = () => {
  showCreateModal.value = true;
};
const closeCreateProjectModal = () => {
  showCreateModal.value = false;
  newProjectName.value = '';
};

// Montaje inicial
onMounted(fetchProjects);
</script>

<style scoped>
.dashboard {
  font-family: Arial, sans-serif;
  text-align: center;
}

.header {
  background-color: #00b8b0;
  color: white;
  padding: 1rem;
}

.main-content {
  padding: 1rem;
}

.projects-container {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  justify-content: center;
  margin-top: 1rem;
}

.project-card {
  background-color: #f0f0f0;
  border: 1px solid #ccc;
  border-radius: 5px;
  padding: 1rem;
  width: 200px;
  text-align: center;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.project-card.add-project {
  cursor: pointer;
  font-weight: bold;
  background-color: #e0f7fa;
}

.delete-button {
  margin-top: 0.5rem;
  background-color: #f44336;
  color: white;
  border: none;
  padding: 0.5rem;
  border-radius: 5px;
  cursor: pointer;
}

.delete-button:hover {
  background-color: #d32f2f;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-content {
  background-color: white;
  padding: 2rem;
  border-radius: 5px;
  width: 300px;
  text-align: center;
}
</style>
