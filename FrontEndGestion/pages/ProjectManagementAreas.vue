<template>
  <div class="container">
    <header class="header">
      <div class="logo-container">
        <div class="logo">USACH</div>
        <h1 class="title">USACH</h1>
      </div>
    </header>

    <main class="main-content">
      <div class="project-info">
        <!-- Mostrar el texto con el nombre del proyecto -->
        <p>Áreas de gestión del proyecto: <strong>{{ projectName }}</strong></p>
      </div>

      <div class="button-container">
        <button @click="goToPage('/project-management')" class="button-square">
          <span class="button-number">1.</span> Gestión de la Integración
        </button>
        <button @click="goToPage('/ScopeManagementView')" class="button-square">
          <span class="button-number">2.</span> Gestión del Alcance
        </button>
        <button  class="button-square">
          <span class="button-number">3.</span> Gestión del Cronograma
        </button>
        <button  class="button-square">
          <span class="button-number">4.</span> Gestión de los Costos
        </button>
        <button  class="button-square">
          <span class="button-number">5.</span> Gestión de calidad
        </button>
        <button  class="button-square">
          <span class="button-number">6.</span> Gestión de las comunicaciones
        </button>

        <button  class="button-square">
          <span class="button-number">7.</span> Gestión de los riesgos
        </button>

        <button  class="button-square">
          <span class="button-number">8.</span> Gestión de las adquisiciones
        </button>

        <button  class="button-square">
          <span class="button-number">9.</span> Gestión de las comunicaciones
        </button>
        <!-- Agregar más botones según sea necesario -->
      </div>
    </main>

    <AlertPopup
      :show="alert.show"
      :title="alert.title"
      :message="alert.message"
      :type="alert.type"
      @confirm="handleAlertConfirm"
    />

    <footer class="footer">
      <p>&copy; 2024 USACH. Todos los derechos reservados.</p>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/auth';
import { useProjectStore } from '../stores/project';
import AlertPopup from '../components/AlertPopup.vue';

const router = useRouter();
const projectName = ref('Cargando...'); // Variable reactiva para el nombre del proyecto
const AuthStore = useAuthStore();
const ProjectStore = useProjectStore();
const loading = ref(false);
const errorMessage = ref('');

const alert = reactive({
  show: false,
  title: '',
  message: '',
  type: 'info',
});

const showAlert = (title, message, type = 'info') => {
  alert.title = title;
  alert.message = message;
  alert.type = type;
  alert.show = true;
};

const handleAlertConfirm = () => {
  alert.show = false;
  if (alert.type === 'error' && alert.message.includes('Sesión no iniciada')) {
    router.push('/login');
  } else if (alert.message.includes('No se ha creado la gestión')) {
    router.push('/ConstitutionForm');
  }
};

// Función para navegar a otra página
function goToPage(path) {
  router.push(path);
}

// Nueva función para manejar la conexión al backend
async function fetchProjectData() {
  try {
    loading.value = true;
    errorMessage.value = '';

    const userId = AuthStore.userId;
    const token = AuthStore.token;
    const projectId = ProjectStore.projectId;

    if (!userId || !token || !projectId) {
      showAlert('Error de Sesión', '¡Sesión no iniciada o proyecto no seleccionado!', 'error');
      return;
    } else {
      const respondeManagement = await fetch(`http://localhost:8080/api/v1/management/getByNameAndIdProyect?nameManagement=Gestión de Integración&idProyect=${projectId}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
      });

      if (!respondeManagement.ok) {
        showAlert('Gestión no encontrada', '¡No se ha creado la gestión de integración!', 'warning');
        return;
      }
    }



    // Obtener la información del proyecto
    const response = await fetch(`http://localhost:8080/api/v1/proyect/getById/${projectId}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
    });

    if (response.ok) {
      const data = await response.json();
      projectName.value = data.nameProyect || 'Proyecto no encontrado';
      console.log('Nombre del proyecto:', projectName.value);
    } else {
      throw new Error('Error al obtener la información del proyecto');
    }
  } catch (error) {
    showAlert('Error', 'Hubo un error al obtener la información del proyecto', 'error');
  } finally {
    loading.value = false;
  }
}

// Llamar a fetchProjectData cuando el componente se monte
onMounted(() => {
  fetchProjectData();
});
</script>

<style scoped>
/* Estilos permanecen iguales */
html,
body {
  height: 100%;
  margin: 0;
  font-family: Arial, sans-serif;
}

.container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.header {
  background-color: white;
  padding: 20px;
  border-bottom: 1px solid #ddd;
}

.logo-container {
  display: flex;
  align-items: center;
  gap: 10px;
}

.logo {
  width: 40px;
  height: 40px;
  background-color: #00b8b0;
  color: white;
  font-weight: bold;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
}

.title {
  color: #333;
  font-size: 30px;
  font-weight: bold;
}

.main-content {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  padding: 40px;
  background-color: white;
}

.project-info {
  margin-bottom: 20px;
  font-size: 18px;
  font-weight: bold;
  color: #000;
}

.button-container {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  width: 70%;
}

.button-square {
  background-color: #d3d3d3;
  color: #333;
  border: none;
  padding: 40px;
  font-size: 20px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease;
  border-radius: 8px;
  text-align: center;
  width: 100%;
  position: relative;
  min-width: 200px;
}

.button-square:hover {
  background-color: #a9a9a9;
}

.button-number {
  position: absolute;
  top: 10px;
  left: 10px;
  font-size: 22px;
  font-weight: bold;
  color: #333;
}

.footer {
  background-color: #00b8b0;
  color: white;
  text-align: center;
  padding: 20px;
  font-size: 18px;
}
</style>
