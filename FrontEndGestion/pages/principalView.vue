<template>
    <div class="dashboard-container">
      <header class="header">
        <div class="logo">
          <img src="../assets/usach-logo.png" alt="USACH" class="usach-logo" />
        </div>
        <div class="welcome-text">
          Hola, {{ userName }}
        </div>
      </header>
  
      <main class="main-content">
        <h2 class="section-title">Mis proyectos</h2>
        
        <div class="projects-grid">
          <!-- Tarjetas de proyectos existentes -->
          <div 
            v-for="project in projects" 
            :key="project.idProyecto" 
            class="project-card"
          >
            <div class="project-header">
              <span class="project-number">{{ project.idProyecto }}</span>
              <div class="project-menu">
                <button class="project-menu-btn" @click="toggleMenu(project.idProyecto)">
                  <span class="dots">⋮</span>
                </button>
                <div v-if="activeMenu === project.idProyecto" class="menu-dropdown">
                  <button class="menu-item delete" @click="confirmDelete(project)">
                    Eliminar proyecto
                  </button>
                </div>
              </div>
            </div>
            <h3 class="project-title">{{ project.nameProyect }}</h3>
            <div class="project-details">
              <p class="project-org">{{ project.organization }}</p>
              <p class="project-dates">
                {{ formatDate(project.startDate) }} - {{ formatDate(project.estimatedEndDate) }}
              </p>
            </div>
          </div>
  
          <!-- Tarjeta para crear nuevo proyecto -->
          <div class="project-card new-project" @click="handleNewProject">
            <div class="new-project-content">
              <span class="plus-icon">+</span>
              <span class="new-project-text">Crear nuevo proyecto</span>
            </div>
          </div>
        </div>
      </main>
    </div>

    <!-- Modal de confirmación -->
    <div v-if="showDeleteModal" class="modal-overlay">
      <div class="modal-content">
        <h3>Confirmar eliminación</h3>
        <p>¿Estás seguro que deseas eliminar el proyecto "{{ selectedProject?.nameProyect }}"?</p>
        <div class="modal-actions">
          <button class="btn-cancel" @click="showDeleteModal = false">Cancelar</button>
          <button class="btn-delete" @click="deleteProject">Eliminar</button>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import { useRouter } from 'vue-router';
  import { useAuthStore } from '../stores/auth';
  
  const router = useRouter();
  const projects = ref([]);
  const userName = ref('');
  const loading = ref(true);
  const error = ref(null);
  const AuthStore = useAuthStore();
  const activeMenu = ref(null);
  const showDeleteModal = ref(false);
  const selectedProject = ref(null);
  
  onMounted(async () => {
    
    if (!AuthStore.token) {
      router.push('/login');
      return;
    }
  
    userName.value = AuthStore.names;
    
    try {
      const response = await fetch(`http://localhost:8080/api/v1/user/${AuthStore.userId}/proyects`, {
        headers: {
          'Authorization': `Bearer ${AuthStore.token}`,
          'Content-Type': 'application/json',
        },
      });
  
      if (!response.ok) {
        throw new Error('Error al cargar los proyectos');
      }
  
      projects.value = await response.json();
    } catch (err) {
      error.value = err.message;
      console.error('Error fetching projects:', err);
    } finally {
      loading.value = false;
    }
  });
  
  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('es-ES', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  };
  
  const handleNewProject = () => {
    router.push('/proyectCreation');
  };

  const toggleMenu = (projectId) => {
    activeMenu.value = activeMenu.value === projectId ? null : projectId;
  };
  
  const confirmDelete = (project) => {
    selectedProject.value = project;
    showDeleteModal.value = true;
    activeMenu.value = null;
  };
  
  const deleteProject = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/v1/proyect/delete/${selectedProject.value.idProyecto}`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${AuthStore.token}`,
        },
      });
  
      if (!response.ok) {
        throw new Error('Error al eliminar el proyecto');
      }
  
      // Eliminar el proyecto de la lista local
      projects.value = projects.value.filter(p => p.idProyecto !== selectedProject.value.idProyecto);
      //mostrar el id del proyecto a elimianr
      console.log(selectedProject.value.idProyecto);
      showDeleteModal.value = false;
      selectedProject.value = null;
  
    } catch (err) {
      console.error('Error deleting project:', err);
      console.log(selectedProject.value.idProyecto);
    }
  };
  </script>
  
  <style scoped>
  .dashboard-container {
    min-height: 100vh;
    background-color: #f5f5f5;
  }
  
  .header {
    background-color: #00B8B0;
    padding: 1rem 2rem;
    display: flex;
    justify-content: space-between;
    align-items: center;
    color: white;
  }
  
  .usach-logo {
    height: 40px;
  }
  
  .welcome-text {
    font-size: 1.1rem;
  }
  
  .main-content {
    padding: 2rem;
    max-width: 1200px;
    margin: 0 auto;
  }
  
  .section-title {
    color: #333;
    margin-bottom: 1.5rem;
    font-size: 1.5rem;
  }
  
  .projects-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 1.5rem;
  }
  
  .project-card {
    background: white;
    border-radius: 8px;
    padding: 1.5rem;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    transition: transform 0.2s, box-shadow 0.2s;
  }
  
  .project-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
  }
  
  .project-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 1rem;
  }
  
  .project-number {
    background-color: #00B8B0;
    color: white;
    padding: 0.25rem 0.75rem;
    border-radius: 12px;
    font-size: 0.9rem;
  }
  
  .project-menu-btn {
    background: none;
    border: none;
    cursor: pointer;
    font-size: 1.5rem;
    color: #666;
    padding: 0.25rem;
  }
  
  .project-title {
    font-size: 1.1rem;
    color: #333;
    margin-bottom: 1rem;
  }
  
  .project-details {
    font-size: 0.9rem;
    color: #666;
  }
  
  .project-org {
    margin-bottom: 0.5rem;
  }
  
  .project-dates {
    color: #888;
  }
  
  .new-project {
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: #f8f8f8;
    border: 2px dashed #ccc;
    cursor: pointer;
  }
  
  .new-project:hover {
    border-color: #00B8B0;
    background-color: #f0f0f0;
  }
  
  .new-project-content {
    text-align: center;
    color: #666;
  }
  
  .plus-icon {
    font-size: 2rem;
    display: block;
    margin-bottom: 0.5rem;
    color: #00B8B0;
  }
  
  .new-project-text {
    font-size: 1rem;
  }
  
  .dots {
    line-height: 1;
  }

  .project-menu {
    position: relative;
  }
  
  .menu-dropdown {
    position: absolute;
    right: 0;
    top: 100%;
    background: white;
    border: 1px solid #eee;
    border-radius: 4px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    z-index: 10;
  }
  
  .menu-item {
    padding: 0.5rem 1rem;
    width: 100%;
    text-align: left;
    border: none;
    background: none;
    cursor: pointer;
    white-space: nowrap;
  }
  
  .menu-item.delete {
    color: #dc3545;
  }
  
  .menu-item.delete:hover {
    background-color: #ffffff;
  }
  
  .modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0,0,0,0.5);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1000;
  }
  
  .modal-content {
    background: white;
    padding: 2rem;
    border-radius: 8px;
    max-width: 400px;
    width: 90%;
    color: #333;
  }
  
  .modal-actions {
    display: flex;
    justify-content: flex-end;
    gap: 1rem;
    margin-top: 1.5rem;
  }
  
  .btn-cancel {
    padding: 0.5rem 1rem;
    border: 1px solid #ddd;
    background: rgb(255, 255, 255);
    border-radius: 4px;
    cursor: pointer;
    color: #333;
  }
  
  .btn-delete {
    padding: 0.5rem 1rem;
    background: #dc3545;
    color: rgb(255, 255, 255);
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
  
  .btn-delete:hover {
    background: #c82333;
  }
  </style>