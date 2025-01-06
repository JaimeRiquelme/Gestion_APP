<template>
  <div class="dashboard-container">
    <header class="header">
      <div class="logo">
        <img src="../assets/usach-logo.png" alt="USACH" class="usach-logo" />
      </div>
    </header>

    <main class="main-content">
      <h2 class="section-title">Áreas de gestión del proyecto: {{ projectName }}</h2>

      <div class="management-grid">
        <button @click="handleManagementArea('Gestion de Integracion', '/IntegrationManagementView')" class="management-card">
          <div class="management-header">
            <span class="management-number">1</span>
          </div>
          <div class="management-content">
            <h3>Gestión de la Integración</h3>
          </div>
        </button>

        <button @click="handleManagementArea('Gestion del Alcance', '/ScopeManagementView')" class="management-card">
          <div class="management-header">
            <span class="management-number">2</span>
          </div>
          <div class="management-content">
            <h3>Gestión del Alcance</h3>
          </div>
        </button>

        <button class="management-card disabled">
          <div class="management-header">
            <span class="management-number">3</span>
          </div>
          <div class="management-content">
            <h3>Gestión del Cronograma</h3>
          </div>
        </button>

        <button class="management-card disabled">
          <div class="management-header">
            <span class="management-number">4</span>
          </div>
          <div class="management-content">
            <h3>Gestión de los Costos</h3>
          </div>
        </button>

        <button class="management-card disabled">
          <div class="management-header">
            <span class="management-number">5</span>
          </div>
          <div class="management-content">
            <h3>Gestión de Calidad</h3>
          </div>
        </button>

        <button class="management-card disabled">
          <div class="management-header">
            <span class="management-number">6</span>
          </div>
          <div class="management-content">
            <h3>Gestión de las Comunicaciones</h3>
          </div>
        </button>

        <button class="management-card disabled">
          <div class="management-header">
            <span class="management-number">7</span>
          </div>
          <div class="management-content">
            <h3>Gestión de los Riesgos</h3>
          </div>
        </button>

        <button class="management-card disabled">
          <div class="management-header">
            <span class="management-number">8</span>
          </div>
          <div class="management-content">
            <h3>Gestión de las Adquisiciones</h3>
          </div>
        </button>

        <button class="management-card disabled">
          <div class="management-header">
            <span class="management-number">9</span>
          </div>
          <div class="management-content">
            <h3>Gestión de las Comunicaciones</h3>
          </div>
        </button>
      </div>
    </main>

    <AlertPopup :show="alert.show" :title="alert.title" :message="alert.message" :type="alert.type"
      @confirm="handleAlertConfirm" />

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
import { useManagementsStore } from '../stores/Managements';
import AlertPopup from '../components/AlertPopup.vue';

const router = useRouter();
const projectName = ref('Cargando...');
const AuthStore = useAuthStore();
const ProjectStore = useProjectStore();
const ManagementStore = useManagementsStore();
const loading = ref(false);
const errorMessage = ref('');
const { fetch } = useFetchWithAuth();

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
      const respondeManagement = await fetch(`http://localhost:8080/api/v1/management/getByNameAndIdProyect?nameManagement=Gestion de Integracion&idProyect=${projectId}`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });

      if (!respondeManagement.ok) {
        showAlert('Gestión no encontrada', '¡No se ha creado la gestión de integración!... Redirigiendo a su creación', 'warning');
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

async function handleManagementArea(managementName, redirectPath) {
  try {
    const token = AuthStore.token;
    const projectId = ProjectStore.projectId;

    if (!token || !projectId) {
      showAlert('Error', 'No hay sesión activa o proyecto seleccionado', 'error');
      return;
    }

    // First, try to get existing management
    const response = await fetch(
      `http://localhost:8080/api/v1/management/getByNameAndIdProyect?nameManagement=${managementName}&idProyect=${projectId}`,
      {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      }
    );

    if (response.ok) {
      const data = await response.json();
      // Store management data and redirect
      ManagementStore.idManagement = data.idManagement;
      ManagementStore.nameManagement = data.nameManagement;
      router.push(redirectPath);
    } else if (response.status === 404) {
      // Create new management if not found
      const createResponse = await fetch('http://localhost:8080/api/v1/management/create', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          idProyecto: projectId,
          nameManagement: managementName,
          description: `${managementName} del proyecto`
        })
      });

      if (createResponse.ok) {
        const newManagement = await createResponse.json();

        ManagementStore.idManagement = newManagement.idManagement;
        ManagementStore.nameManagement = newManagement.nameManagement;

        router.push(redirectPath);
      } else {
        showAlert('Error', 'No se pudo crear la gestión', 'error');
      }
    }
  } catch (error) {
    showAlert('Error', 'Error al procesar la solicitud', 'error');
    console.error(error);
  }
}

// Llamar a fetchProjectData cuando el componente se monte
onMounted(() => {
  fetchProjectData();
});
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

.management-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 1.5rem;
}

.management-card {
  background: white;
  border: none;
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s, box-shadow 0.2s;
  cursor: pointer;
  width: 100%;
  text-align: left;
}

.management-card:not(.disabled):hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.management-card.disabled {
  opacity: 0.6;
  cursor: not-allowed;
  background-color: #f0f0f0;
}

.management-header {
  margin-bottom: 1rem;
}

.management-number {
  background-color: #00B8B0;
  color: white;
  padding: 0.25rem 0.75rem;
  border-radius: 12px;
  font-size: 0.9rem;
}

.management-content h3 {
  font-size: 1.1rem;
  color: #333;
  margin: 0;
}

.footer {
  background-color: #00B8B0;
  color: white;
  text-align: center;
  padding: 1rem;
  margin-top: auto;
}
</style>