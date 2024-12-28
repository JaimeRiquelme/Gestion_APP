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
              <button class="project-menu-btn">
                <span class="dots">⋮</span>
              </button>
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
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import { useRouter } from 'vue-router';
  import { useCookie } from 'nuxt/app';
  
  const router = useRouter();
  const projects = ref([]);
  const userName = ref('');
  const loading = ref(true);
  const error = ref(null);
  
  onMounted(async () => {
    const userIdCookie = useCookie('userId');
    const namesCookie = useCookie('names');
    const tokenCookie = useCookie('authToken');
    
    if (!userIdCookie.value || !tokenCookie.value) {
      router.push('/login');
      return;
    }
  
    userName.value = namesCookie.value || 'usuario';
    
    try {
      const response = await fetch(`http://localhost:8080/api/v1/user/${userIdCookie.value}/proyects`, {
        headers: {
          'Authorization': `Bearer ${tokenCookie.value}`,
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
  </style>