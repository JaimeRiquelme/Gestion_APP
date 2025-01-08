<template>
  <div class="page-container">
    <main class="main-content">
      <div class="title-content">
        <h1 class="title-content-text">Plan de Gestión del Alcance</h1>
      </div>

      <div class="form-container">
        <!-- Sección de Información Básica (Siempre visible) -->
        <!-- Sección de Información Básica (Solo visible en la primera vista) -->
        <section v-if="count === 0" class="form-section">
          <h2 class="section-title">Información Básica</h2>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div class="form-group">
              <label for="projectName">Nombre del Proyecto *</label>
              <input id="projectName" v-model="formData.proyectName" type="text" class="form-input" readonly required />
            </div>

            <div class="form-group">
              <label for="idProject">ID del Proyecto *</label>
              <input id="idProject" v-model="formData.idProject" type="text" class="form-input" readonly required />
            </div>

            <div class="form-group">
              <label for="projectLeader">Líder del Proyecto *</label>
              <input id="projectLeader" v-model="formData.projectLeader" type="text" class="form-input" readonly
                required />
            </div>
            <div class="form-group">
              <label for="productLeader">Líder del Producto *</label>
              <input id="productLeader" v-model="formData.productLeader" type="text" class="form-input" required />
            </div>

            <div class="form-group">
              <label for="elaborationDate">Fecha de Elaboración *</label>
              <input id="elaborationDate" v-model="formData.elaborationDate" type="date" class="form-input" readonly
                required />
            </div>
          </div>
        </section>

        <!-- Sección de Introducción (en la misma vista que Información Básica) -->
        <section v-if="count === 0" class="form-section">
          <h2 class="section-title">Introducción</h2>
          <div class="content">
            <p class="description-text">
              La gestión del alcance es la colección de procesos que aseguran que el proyecto incluye todo el trabajo
              requerido para completarlo a la vez que excluye todo el trabajo que no es necesario para completarlo. El
              plan detalla cómo el alcance será definido, desarrollado y verificado, y define quién será responsable de
              gestionar el alcance del proyecto.
            </p>
            <div class="form-group">
              <label for="introduction">Introducción del Proyecto *</label>
              <textarea id="introduction" v-model="formData.introduction" class="form-textarea"
                placeholder="Describa la introducción del proyecto..." rows="6" required></textarea>
            </div>
          </div>
        </section>

        <!-- Sección de Enfoque de Gestión -->
        <section v-if="count === 1" class="form-section">
          <h2 class="section-title">Enfoque de Gestión de Alcance</h2>
          <div class="content">
            <p class="description-text">
              El enfoque para la gestión del alcance entrega un resumen del plan de gestión del alcance, incluyendo
              quién tiene la autoridad y responsabilidad de gestionarlo, de qué manera es definido el alcance, cómo se
              verifica y mide el alcance.
            </p>
            <div class="form-group">
              <label for="scopeManagementApproach">Enfoque de Gestión *</label>
              <textarea id="scopeManagementApproach" v-model="formData.scopeManagementApproach" class="form-textarea"
                placeholder="Describa el enfoque de gestión..." rows="6" required></textarea>
            </div>
          </div>
        </section>

        <!-- Sección de rolesAndResponsibilities y Responsabilidades -->
        <section v-if="count === 2" class="form-section">
          <h2 class="section-title">rolesAndResponsibilities y Responsabilidades</h2>
          <div class="content">
            <div class="rolesAndResponsibilities-container">
              <table class="rolesAndResponsibilities-table">
                <thead>
                  <tr>
                    <th>Nombre</th>
                    <th>Rol</th>
                    <th>Responsabilidades</th>
                    <th>Acción</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(role, index) in rolesAndResponsibilities" :key="index" class="role-row">
                    <td>
                      <input v-model="role.name" placeholder="Nombre" class="form-input" />
                    </td>
                    <td>
                      <input v-model="role.role" placeholder="Rol" class="form-input" />
                    </td>
                    <td>
                      <input v-model="role.responsibilities" placeholder="Responsabilidades" class="form-input" />
                    </td>
                    <td>
                      <button @click="removeRole(index)" class="delete-button" :disabled="rolesAndResponsibilities.length === 1">
                        Eliminar
                      </button>
                    </td>
                  </tr>
                </tbody>
              </table>
              <button @click="addRole" class="add-button">
                <span>+</span> Agregar Rol
              </button>
            </div>
          </div>
        </section>

        <!-- Definición del Alcance -->
        <section v-if="count === 3" class="form-section">
          <h2 class="section-title">Definición del Alcance</h2>
          <div class="content">
            <p class="description-text">
              La definición del alcance señala el proceso de desarrollar una descripción detallada del proyecto y de sus
              entregables.
            </p>
            <div class="form-group">
              <label for="scopeDefinition">Definición del Alcance *</label>
              <textarea id="scopeDefinition" v-model="formData.scopeDefinition" class="form-textarea"
                placeholder="Describa la definición del alcance..." rows="6" required></textarea>
            </div>
          </div>
        </section>

        <!-- Plan de Gestión del Alcance -->
        <section v-if="count === 4" class="form-section">
          <h2 class="section-title">Plan de Gestión del Alcance</h2>
          <div class="content">
            <p class="description-text">
              Para el plan de gestión del alcance existen dos herramientas adicionales que se pueden rellenar en esta
              aplicación.
            </p>
            <div class="tools-buttons">
              <button @click="navigateTo('/wbs')" class="tool-button">
                Estructura de Desglose del Trabajo (EDT)
              </button>
              <button @click="navigateTo('/scopeStatement')" class="tool-button">
                Enunciado del Alcance del Proyecto
              </button>
            </div>
          </div>
        </section>

        <!-- Verificación del Alcance -->
        <section v-if="count === 5" class="form-section">
          <h2 class="section-title">Verificación del Alcance</h2>
          <div class="content">
            <p class="description-text">
              La verificación del alcance señala cómo los entregables serán validados en comparación con el alcance
              original.
            </p>
            <div class="form-group">
              <label for="scopeVerification">Verificación del Alcance *</label>
              <textarea id="scopeVerification" v-model="formData.scopeVerification" class="form-textarea"
                placeholder="Describa la verificación del alcance..." rows="6" required></textarea>
            </div>
          </div>
        </section>

        <!-- Control del Alcance -->
        <section v-if="count === 6" class="form-section">
          <h2 class="section-title">Control del Alcance</h2>
          <div class="content">
            <p class="description-text">
              El control del alcance es el proceso de monitorear el estado del alcance del proyecto y gestionar cambios
              en la línea base.
            </p>
            <div class="form-group">
              <label for="scopeControl">Control del Alcance *</label>
              <textarea id="scopeControl" v-model="formData.scopeControl" class="form-textarea"
                placeholder="Describa el control del alcance..." rows="6" required></textarea>
            </div>
          </div>
        </section>

        <!-- Botones de navegación y acciones -->
        <div class="form-actions">
          <div class="button-group">
            <!-- Botón Anterior -->
            <button v-if="count > 0" type="button" class="interact-button" @click="handleBack">
              Anterior
            </button>

            <!-- Botón Siguiente -->
            <button v-if="count < 6" type="button" class="interact-button" @click="handleForward">
              Siguiente
            </button>

            <!-- Botón Guardar -->
            <button type="button" class="save-button" :disabled="loading" @click="handleSave">
              {{ loading ? 'Guardando...' : 'Guardar' }}
            </button>

            <!-- Botón Cancelar -->
            <button type="button" class="cancel-button" @click="showCancelConfirmation = true">
              Cancelar
            </button>

            <!-- Botón Crear Documento (solo en la última página) -->
            <button v-if="count === 6" type="submit" class="submit-button" :disabled="loading" @click="handleSubmit">
              {{ loading ? 'Creando...' : 'Crear Documento' }}
            </button>
          </div>
        </div>

        <!-- Modal de Confirmación de Cancelación -->
        <div v-if="showCancelConfirmation" class="modal-overlay">
          <div class="modal-content">
            <h3 class="modal-title">Confirmar Cancelación</h3>
            <p class="modal-message">¿Estás seguro que deseas cancelar? Los cambios no guardados se perderán.</p>
            <div class="modal-actions">
              <button class="modal-button secondary" @click="showCancelConfirmation = false">
                No, continuar editando
              </button>
              <button class="modal-button primary" @click="handleCancel">
                Sí, cancelar
              </button>
            </div>
          </div>
        </div>

        <!-- Mensaje de Error -->
        <div v-if="errorMessage" class="error-message">
          {{ errorMessage }}
        </div>

        <!-- Vista Previa del PDF -->
        <div v-if="pdfUrl" class="pdf-container">
          <div class="pdf-header">
            <h2 class="pdf-title">Vista previa del documento</h2>
            <div class="pdf-actions">
              <a :href="pdfUrl" download="PlanGestionAlcance.pdf" class="download-button">
                <span class="button-icon">↓</span>
                Descargar PDF
              </a>
            </div>
          </div>
          <div class="pdf-viewer">
            <iframe :src="pdfUrl" class="pdf-iframe"></iframe>
          </div>
          <div class="pdf-footer">
            <button @click="navigateTo('/ScopeManagementView')" class="return-button">
              Volver al Dashboard
            </button>
          </div>
        </div>
      </div>
    </main>
  </div>

</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useAuthStore } from '../stores/auth';
import { useProjectStore } from '../stores/project';
import { useProcessStore } from '../stores/Process';

const loading = ref(false);
const errorMessage = ref('');
const AuthStore = useAuthStore();
const ProjectStore = useProjectStore();
const pdfUrl = ref(null);
const invalidFields = ref(new Set());
const showCancelConfirmation = ref(false);
const count = ref(0);
const ProcessStore = useProcessStore();
const { fetch } = useFetchWithAuth();

const url_doc_generator = "http://localhost:8080/api/documents/scope-management-plan";
const url_exit = "http://localhost:8080/api/v1/exit";
const url_process = "http://localhost:8080/api/v1/process";

const rolesAndResponsibilities = reactive([
  { name: '', role: '', responsibilities: '' }
]);



const formData = reactive({
  proyectName: "",
  idProject: "",
  productLeader: "",
  projectLeader: "",
  elaborationDate: new Date().toISOString().split('T')[0],
  introduction: '',
  scopeManagementApproach: '',
  rolesAndResponsibilities: [{
    name: '',
    role: '',
    responsibilities: ''
  }],
  scopeDefinition: '',
  scopeVerification: '',
  scopeControl: '',
});

const createNewExit = async (processId, nameUser, token) => {
  const createExitResponse = await fetch(`http://localhost:8080/api/v1/exit/create`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      idProcess: processId,
      nameExit: 'Plan de Gestión del Alcance',
      state: 'Activo',
      dateCreation: new Date().toISOString().split('T')[0],
      dateValidation: new Date().toISOString().split('T')[0],
      priority: 'Alta',
      responsible: nameUser,
      description: 'Plan de Gestión del Alcance del Proyecto',
    })
  });

  const responseText = await createExitResponse.text();
  if (!responseText.trim()) {
    throw new Error('No se recibió ningún contenido JSON');
  }

  if (!createExitResponse.ok) {
    throw new Error(`Error al crear la salida: ${responseText}`);
  }

  const newExit = JSON.parse(responseText);
  return newExit.idExit;
};

const getExistingExit = async (processId, token) => {
  const response = await fetch(
    `http://localhost:8080/api/v1/exit/getByIdProcessAndNameExit?idProcess=${processId}&nameExit=Plan de Gestión del Alcance`,
    {
      headers: { 'Authorization': `Bearer ${token}` },
    }
  );

  if (response.ok) {
    const textData = await response.text();
    if (!textData.trim()) {
      return null; // Indicate need to create new exit
    }
    const exitData = JSON.parse(textData);
    return exitData.idExit;
  }
  return null;
};

const handleSubmit = async () => {
  if (!validateForm()) return;

  try {
    loading.value = true;
    const { userId, token, name: nameUser } = AuthStore;
    const processId = ProcessStore.processId;

    if (!userId || !token) {
      router.push('/login');
      return;
    }

    let exitId = await getExistingExit(processId, token);
    if (!exitId) {
      exitId = await createNewExit(processId, nameUser, token);
    }

    const docPayload = {
      ...formData,
      rolesAndResponsibilities: formatrolesAndResponsibilities(rolesAndResponsibilities),
    };

    const generateDocResponse = await fetch(
      `http://localhost:8080/api/documents/scope-management-plan/generate?idExit=${exitId}`,
      {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(docPayload)
      }
    );

    if (!generateDocResponse.ok) {
      throw new Error('Error al generar el documento');
    }

    const blob = await generateDocResponse.blob();
    pdfUrl.value = URL.createObjectURL(blob);
  } catch (error) {
    showAlert('Error', error.message, 'error');
  } finally {
    loading.value = false;
  }
};

const handleCancel = () => {
  showCancelConfirmation.value = false;
  navigateTo('/ScopeManagementView');
};

const handleBack = () => {
  count.value -= 1;
};

const handleForward = () => {
  count.value += 1;
};

const addRole = () => {
  rolesAndResponsibilities.push({ name: '', role: '', responsibilities: '' });
};

const removeRole = (index) => {
  rolesAndResponsibilities.splice(index, 1);
};

const formatrolesAndResponsibilities = (data) => {
  let role = "&Nombre,Rol,Resposabilidad&";
  data.forEach(req => {
    role += `${req.name},${req.role},${req.responsibilities}&`;
  });
  return role;
};

const handleSave = async () => {
  try {
    const userId = AuthStore.userId;
    const token = AuthStore.token;
    const projectId = ProjectStore.projectId;
    const dataToSend = {
      ...formData,
      rolesAndResponsibilities: formatrolesAndResponsibilities(rolesAndResponsibilities),
    };
    console.log('Saving data:', formData, rolesAndResponsibilities);
    const exitId = await getExistingExit(ProcessStore.processId, token);
    let finalExitId = exitId;
    console.log('Exit ID:', exitId);
    if (!exitId) {
      finalExitId = await createNewExit(ProcessStore.processId, AuthStore.name, token);
    }

    const response = await fetch(
      `http://localhost:8080/api/v1/parameters/saveParametersList?idExit=${finalExitId}`,
      {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(dataToSend),
      }
    );
    console.log('Response:', response);
    // Modificación aquí para manejar la respuesta
    if (!response.ok) {
      throw new Error('Error al guardar los datos');
    }

    const responseText = await response.text();

    // Si la respuesta está vacía pero el status es 201/200, consideramos que fue exitoso
    if (response.ok && (!responseText || responseText.trim() === '')) {
      return { success: true, message: 'Datos guardados correctamente' };
    }

    // Solo intentamos parsear como JSON si hay contenido
    try {
      const responseData = JSON.parse(responseText);
      return responseData;
    } catch {
      // Si hay contenido pero no es JSON válido, igual consideramos exitoso si el status es ok
      if (response.ok) {
        return { success: true, message: 'Datos guardados correctamente' };
      }
      throw new Error('Respuesta inválida del servidor');
    }

  } catch (error) {
    console.error('Error en handleSave:', error);
    throw error;
  }
};

const validateForm = () => {
  if (
    !formData.productLeader ||
    !formData.introduction ||
    !formData.scopeManagementApproach ||
    !formData.scopeDefinition ||
    !formData.scopeVerification ||
    !formData.scopeControl
  ) {
    return false;
  }
  return rolesAndResponsibilities.every(
    role => role.name && role.role && role.responsibilities
  );
};

onMounted(async () => {
  try {
    loading.value = true;
    const projectId = ProjectStore.projectId;
    const token = AuthStore.token;
    const userName = AuthStore.names;

    // Primera petición para obtener datos del proyecto
    const projectResponse = await fetch(`http://localhost:8080/api/v1/proyect/getById/${projectId}`, {
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });

    if (projectResponse.ok) {
      const projectData = await projectResponse.json();
      console.log('Project response:', projectData); // Mueve el console.log aquí
      formData.proyectName = projectData.nameProyect;
      formData.idProject = projectData.idProyecto;
      formData.elaborationDate = projectData.startDate;
      formData.projectLeader = userName;
    }

    // Obtener el exitId
    const exitId = await getExistingExit(ProcessStore.processId, token);

    if (exitId) {
      // Segunda petición para obtener los parámetros
      const parametersResponse = await fetch(
        `http://localhost:8080/api/v1/exit/${exitId}/parameters`,
        {
          headers: {
            'Authorization': `Bearer ${token}`,
          },
        }
      );

      if (parametersResponse.ok) {
        const parameters = await parametersResponse.json();

        // Mapear los parámetros a los campos del formulario
        parameters.forEach(param => {
          console.log(`Processing parameter: ${param.nameParameter}`, param.content);
          switch (param.nameParameter) {
            case 'proyectName':
              formData.projectName = param.content;
              break;

            case 'idProyect':
              formData.idProject = param.content;
              break;

            case 'elaborationDate':
              formData.elaborationDate = param.content;
              break;

            case 'introduction':
              formData.introduction = param.content;
              break;

            case 'scopeManagementApproach':
              formData.scopeManagementApproach = param.content;
              break;

            case 'scopeDefinition':
              formData.scopeDefinition = param.content;
              break;

            case 'scopeVerification':
              formData.scopeVerification = param.content;
              break;

            case 'scopeControl':
              formData.scopeControl = param.content;
              break;

            case 'rolesAndResponsibilities': {
              rolesAndResponsibilities.splice(0, rolesAndResponsibilities.length);
              const dataRows = param.content.split('&').slice(2, -1);
              dataRows.forEach(row => {
                const [name, role, responsibilities] = row.split(',');
                rolesAndResponsibilities.push({
                  name: name || '',
                  role: role || '',
                  responsibilities: responsibilities || ''
                });
              });
              break;
            }
            case 'productLeader':
              formData.productLeader = param.content;
              break;
            case 'projectLeader':
              formData.projectLeader = param.content;
              break;
            default:
              console.log(`Parámetro no manejado: ${param.nameParameter}`);
              break;
          }
        });
      }
    }
  } catch (error) {
    showAlert('Error', 'Error al cargar los datos del proyecto', 'error');
    console.error('Error:', error);
  } finally {
    loading.value = false;
  }
});

const alert = reactive({
  show: false,
  title: '',
  message: '',
  type: 'info'
});

// Añade la función showAlert
const showAlert = (title, message, type = 'info') => {
  alert.title = title;
  alert.message = message;
  alert.type = type;
  alert.show = true;
};

// Añade la función para manejar la confirmación del alert
const handleAlertConfirm = () => {
  alert.show = false;
  if (alert.type === 'error' && alert.message.includes('Sesión no iniciada')) {
    navigateTo('/login');
  }
};

const parserolesAndResponsibilities = (content) => {
  try {
    const rows = content.split('&');
    // Ignorar la fila de encabezados y el último elemento vacío
    const dataRows = rows.slice(2, -1);

    if (dataRows.length === 0) {
      return [{
        name: '',
        rol: '',
        responsibilities: ''
      }];
    }

    return dataRows.map(row => {
      const [name, rol, responsibilities] = row.split(',');
      return {
        name: name || '',
        rol: rol || '',
        responsibilities: responsibilities || ''
      };
    });
  } catch (error) {
    console.error('Error parsing high level requirements:', error);
    return [{
      name: '',
      rol: '',
      responsibilities: ''
    }];
  }
};
</script>

<style scoped>
.page-container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 2rem 1rem;
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
}

.title-content {
  background-color: #f5f5f5;
  padding: 1.5rem;
  margin-bottom: 2rem;
  text-align: center;
}

.title-content-text {
  font-size: 1.875rem;
  font-weight: 600;
  color: #000000;
}

.form-container {
  background: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  padding: 2rem;
}

.form-section {
  border-bottom: 1px solid #eee;
  padding-bottom: 2rem;
  margin-bottom: 2rem;
}

.section-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #000000;
  margin-bottom: 1.5rem;
}

.description-text {
  color: #666;
  margin-bottom: 1.5rem;
  line-height: 1.6;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  font-size: 0.875rem;
  font-weight: 500;
  color: #374151;
  margin-bottom: 0.5rem;
}

.form-input,
.form-textarea {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #d1d5db;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  color: #000000;
  background-color: #ffffff;
  transition: all 0.2s;
}

.form-input:focus,
.form-textarea:focus {
  outline: none;
  border-color: #00B8B0;
  box-shadow: 0 0 0 2px rgba(0, 184, 176, 0.1);
}

.form-textarea {
  min-height: 120px;
  resize: vertical;
}

.rolesAndResponsibilities-container {
  margin-top: 1.5rem;
}

.rolesAndResponsibilities-table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
  margin-bottom: 1.5rem;
}

.rolesAndResponsibilities-table th {
  background-color: #f8f9fa;
  padding: 1rem;
  font-weight: 600;
  color: #374151;
  text-align: left;
  border-bottom: 2px solid #e5e7eb;
}

.rolesAndResponsibilities-table td {
  padding: 1rem;
  border-bottom: 1px solid #e5e7eb;
}

.role-row:hover {
  background-color: #f9fafb;
}

.delete-button {
  padding: 0.5rem 1rem;
  background-color: #ef4444;
  color: white;
  border: none;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  cursor: pointer;
  transition: background-color 0.2s;
}

.delete-button:hover:not(:disabled) {
  background-color: #dc2626;
}

.delete-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.add-button {
  display: inline-flex;
  align-items: center;
  padding: 0.75rem 1.5rem;
  background-color: #00B8B0;
  color: white;
  border: none;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.add-button:hover {
  background-color: #009B94;
}

.add-button span {
  margin-right: 0.5rem;
  font-size: 1.25rem;
}

.tools-buttons {
  display: flex;
  gap: 1rem;
  margin-top: 1.5rem;
}

.tool-button {
  padding: 0.75rem 1.5rem;
  background-color: #00B8B0;
  color: white;
  border: none;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.tool-button:hover {
  background-color: #009B94;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 2rem;
  padding-top: 2rem;
  border-top: 1px solid #e5e7eb;
}

.button-group {
  display: flex;
  gap: 1rem;
}

.interact-button,
.submit-button,
.cancel-button,
.save-button {
  padding: 0.75rem 1.5rem;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.interact-button {
  background-color: #00B8B0;
  color: white;
  border: none;
}

.interact-button:hover {
  background-color: #009B94;
}

.save-button {
  background-color: #10b981;
  color: white;
  border: none;
}

.save-button:hover:not(:disabled) {
  background-color: #059669;
}

.save-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.cancel-button {
  background-color: #ef4444;
  color: white;
  border: none;
}

.cancel-button:hover {
  background-color: #dc2626;
}

.submit-button {
  background-color: #00B8B0;
  color: white;
  border: none;
}

.modal-content.max-w-3xl {
  max-width: 48rem;
}

.modal-content pre {
  max-height: 60vh;
  overflow-y: auto;
}

.bg-gray-100 {
  background-color: #f3f4f6;
}

.whitespace-pre-wrap {
  white-space: pre-wrap;
}

.text-sm {
  font-size: 0.875rem;
}

.p-4 {
  padding: 1rem;
}

.rounded-md {
  border-radius: 0.375rem;
}

.my-4 {
  margin-top: 1rem;
  margin-bottom: 1rem;
}

.submit-button:hover:not(:disabled) {
  background-color: #009B94;
}

.submit-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* Modal Styles */
.modal-overlay {
  position: fixed;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 50;
}

.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 0.5rem;
  max-width: 28rem;
  width: 90%;
}

.modal-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #000000;
  margin-bottom: 1rem;
}

.modal-message {
  color: #4b5563;
  margin-bottom: 1.5rem;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
}

.modal-button {
  padding: 0.75rem 1.5rem;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.modal-button.primary {
  background-color: #ef4444;
  color: white;
  border: none;
}

.modal-button.primary:hover {
  background-color: #dc2626;
}

.modal-button.secondary {
  background-color: #f3f4f6;
  color: #374151;
  border: 1px solid #d1d5db;
}

.modal-button.secondary:hover {
  background-color: #e5e7eb;
}

/* PDF Viewer Styles */
.pdf-container {
  margin-top: 2rem;
  background: white;
  border-radius: 0.5rem;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  padding: 1.5rem;
}

.pdf-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.pdf-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #000000;
}

.pdf-actions {
  display: flex;
  gap: 1rem;
}

.download-button {
  display: inline-flex;
  align-items: center;
  padding: 0.75rem 1.5rem;
  background-color: #00B8B0;
  color: white;
  border: none;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  text-decoration: none;
  transition: background-color 0.2s;
}

.download-button:hover {
  background-color: #009B94;
}

.button-icon {
  margin-right: 0.5rem;
}

.pdf-viewer {
  background-color: #f9fafb;
  padding: 1rem;
  border-radius: 0.375rem;
  margin-bottom: 1.5rem;
}

.pdf-iframe {
  width: 100%;
  height: 700px;
  border: none;
  border-radius: 0.375rem;
}

.pdf-footer {
  display: flex;
  justify-content: flex-end;
  padding-top: 1.5rem;
  border-top: 1px solid #e5e7eb;
}

.return-button {
  padding: 0.75rem 1.5rem;
  background-color: #f3f4f6;
  color: #374151;
  border: 1px solid #d1d5db;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.return-button:hover {
  background-color: #e5e7eb;
}

.error-message {
  margin-top: 1rem;
  padding: 1rem;
  background-color: #fee2e2;
  color: #ef4444;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  text-align: center;
}

/* Responsive Styles */
@media (max-width: 640px) {
  .form-container {
    padding: 1rem;
  }

  .button-group {
    flex-direction: column;
  }

  .form-actions button {
    width: 100%;
  }

  .tools-buttons {
    flex-direction: column;
  }
}
</style>
``` 