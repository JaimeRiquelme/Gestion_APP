<template>
  <div class="page-container">
    <!--Página 1: Introducción-->
    <div v-if="count==0" class="page">
      <main class="content">
        <section class="introduction">
          <h2>Introducción</h2>
          <p>
            La gestión del alcance es la colección de procesos que aseguran que el proyecto incluye todo el trabajo requerido para completarlo a la vez que excluye todo el trabajo que no es necesario para completarlo. 
            El plan detalla cómo el alcance será definido, desarrollado y verificado, y define quién será responsable de gestionar el alcance del proyecto. Hay cinco procesos principales: Recolectar requisitos, definir alcance, crear EDT, verificar alcance y controlar el alcance.
          </p>
          <textarea v-model="formData.introduction" class="form-input form-textarea" placeholder="Introducción" required></textarea>
        </section>
      </main>
    </div>

    <!--Página 2: Enfoque de Gestión de Alcance-->
    <div v-if="count==1" class="page">
      <main class="content">
        <section class="introduction">
          <h2>Enfoque de Gestión de Alcance</h2>
          <p>
            El enfoque para la gestión del alcance entrega un resumen del plan de gestión del alcance, incluyendo quién tiene la autoridad y responsabilidad de gestionarlo, de qué manera es definido el alcance, cómo se verifica y mide el alcance, el proceso de cambio de alcance y quién es responsable de aceptar el entregable final y aprueba el alcance del proyecto.
          </p>
          <textarea v-model="formData.scopeManagementApproach" class="form-input form-textarea" placeholder="Enfoque de Gestión de Alcance" required></textarea>
        </section>
      </main>
    </div>

    <!--Página 3: Roles y Responsabilidades-->
    <div v-if="count==2" class="page">
      <main class="content">
        <section class="introduction">
          <h2>Roles y Responsabilidades</h2>
          <table class="roles-table">
            <thead>
              <tr>
                <th>Nombre</th>
                <th>Rol</th>
                <th>Responsabilidades</th>
                <th>Acción</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(role, index) in roles" :key="index">
                <td>
                  <input
                    v-model="role.name"
                    placeholder="Nombre"
                    class="input-field"
                  />
                </td>
                <td>
                  <input
                    v-model="role.role"
                    placeholder="Rol"
                    class="input-field"
                  />
                </td>
                <td>
                  <input
                    v-model="role.responsibilities"
                    placeholder="Responsabilidades"
                    class="input-field"
                  />
                </td>
                <td>
                  <button @click="removeRole(index)" class="delete-button">
                    Eliminar
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
          <button @click="addRole" class="add-button">Agregar</button>
        </section>
      </main>
    </div>

    <!--Página 4:  Definición del Alcance-->
    <div v-if="count==3" class="page">
      <main class="content">
        <section class="introduction">
          <h2>Definición del alcance</h2>
          <p>
            La definición del alcance señala el proceso de desarrollar una descripción detallada del proyecto y de sus entregables.
            Esta sólo puede ser completada una vez se hayan identificado y definifo los requisitos.
            Recuerda que en esta herraienta se utilizan el plan de gestión de requisitos, la documentación de requisitos y la matriz de trazabilidad de trazabilidad.
          </p>
          <textarea v-model="formData.scopeDefinition" class="form-input form-textarea" placeholder="Introducción" required></textarea>
        </section>
      </main>
    </div>

    <!--Página 5:  Plan de gestión del alcance-->
    <div v-if="count==4" class="page">
      <main class="content">
        <section class="introduction">
          <h2>Plan de gestión del alcance</h2>
          <p>
            Para el plan de gestión del alcance existen dos herramientas adicionales que se pueden rellenar en esta aplicación.
            Si ya los completaste, puedes continuar hacia las siguientes secciónes, de lo contrario, rellénalas aquí:
          </p>
          <button class="interact-button" @click="navigateTo('/wbs')">Estructura de Desglose del Trabajo (EDT)</button>
          <button class="interact-button" @click="navigateTo('/scopeStatement')">Enunciado del alcance del proyecto</button>
        </section>
      </main>
    </div>

    <!--Página 6:  Definición del Alcance-->
    <div v-if="count==5" class="page">
      <main class="content">
        <section class="introduction">
          <h2>Verificación del alcance</h2>
          <p>
            La verificación del alcance señala cómo los entregables serán validados en comparación con el alcance original y cómo los entregables del proyecto serán formalmente aceptados.
            Los entregables deberían ser formalmente aceptados y firmados por el cliente durante el ciclo de vida del proyecto, en lugar de un solo entregable al final del proyecto.
          </p>
          <textarea v-model="formData.scopeVerification" class="form-input form-textarea" placeholder="Introducción" required></textarea>
        </section>
      </main>
    </div>

    <!--Página 7:  Control del Alcance-->
    <div v-if="count==6" class="page">
      <main class="content">
        <section class="introduction">
          <h2>Verificación del alcance</h2>
          <p>
            El control del alcance es el proceso de moniterear el estado del alcance del proyecto, es decir, cómo se previenen los cambios en el alcance.
            Esta sección también detalla el proceso necesario realizar cambios a la línea base del alcance.
          </p>
          <textarea v-model="formData.scopeControl" class="form-input form-textarea" placeholder="Introducción" required></textarea>
        </section>
      </main>
    </div>

    <!-- Botones -->
    
    <div class="form-actions">
      <div v-if="count != 0">
      <button type="button" class="interact-button" @click="handleBack">
        Volver
      </button>
      </div>
      <div v-if="count != 6">
      <button type="button" class="interact-button" @click="handleForward">
        Continuar
      </button>
      </div>
      <button type="button" class="cancel-button" @click="showCancelConfirmation = true">
        Salir
      </button>
      <button type="button" class="save-button" :disabled="loading" @click="handleSave">
        {{ loading ? 'Guardando...' : 'Guardar' }}
      </button>
      <button type="submit" class="submit-button" :disabled="loading" @click="handleSubmit">
        {{ loading ? 'Creando...' : 'Crear Documento' }}
      </button>
    </div>
            
    <!-- Diálogo de confirmación para cancelar -->
    <div v-if="showCancelConfirmation" class="modal-overlay">
        <div class="modal-content">
            <h3>Confirmar Cancelación</h3>
            <p>¿Estás seguro que deseas cancelar? Los cambios no guardados se perderán.</p>
            <div class="modal-actions">
            <button class="cancel-button" @click="showCancelConfirmation = false">
                No, continuar editando
            </button>
            <button class="confirm-button" @click="handleCancel">
                Sí, cancelar
            </button>
            </div>
        </div>
    </div>
        
    <div v-if="errorMessage" class="error-message">
        {{ errorMessage }}
    </div>

    <div v-if="pdfUrl" class="pdf-container">
        <div class="pdf-header">
            <h2 class="pdf-title">Vista previa del documento</h2>
            <div class="pdf-actions">
                <a :href="pdfUrl" download="PlanGestionDeRequisitos.pdf" class="pdf-button download-button">
                    <span class="button-icon">↓</span>
                    Descargar PDF
                </a>
            </div>
        </div>

        <div class="pdf-viewer">
            <iframe :src="pdfUrl" class="pdf-iframe"></iframe>
        </div>

        <div class="pdf-footer">
            <button @click="navigateTo('/dashboard')" class="pdf-button return-button">
                Volver al Dashboard
            </button>
        </div>

    </div>

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

const url_doc_generator = "http://localhost:8080/api/documents/scope-management-plan";
const url_exit = "http://localhost:8080/api/v1/exit";
const url_process = "http://localhost:8080/api/v1/process";

const roles = reactive([
  { name: '', role: '', responsibilities: '' }
]);


const formData = reactive({
    projectName: "",
    idProject: "",
    projectLeader: "",
    elaborationDate: new Date().toISOString().split('T')[0],
    introduction: '',
    scopeManagementApproach: '',
    roles: [{
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

        // Get or create exit
        let exitId = await getExistingExit(processId, token);
        if (!exitId) {
            exitId = await createNewExit(processId, nameUser, token);
        }

        // Generate document
        const formattedData = {
            ...formData,
            roles: formatRoles(roles),
        };

        const response = await fetch(
            `http://localhost:8080/api/documents/project-scope-statement/generate?idExit=${exitId}`,
            {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formattedData),
            }
        );

        if (!response.ok) {
            throw new Error(await response.text());
        }

        const blob = await response.blob();
        window.open(URL.createObjectURL(blob));
        router.push('/ScopeManagementView');

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
  roles.push({ name: '', role: '', responsibilities: '' });
};

const removeRole = (index) => {
    roles.splice(index, 1);
};

const formatRoles= (data) => {
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
            roles: formatRoles(roles),
        };
        console.log('Saving data:', formData, roles);
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
    !formData.introduction ||
    !formData.scopeManagementApproach ||
    !formData.scopeDefinition ||
    !formData.scopeVerification ||
    !formData.scopeControl
  ) {
    return false;
  }

  return roles.every(role => role.name && role.role && role.responsibilities);
};

onMounted(async () => {
    try {
        loading.value = true;
        const projectId = ProjectStore.projectId;
        const token = AuthStore.token;

        // Primera petición para obtener datos del proyecto
        const projectResponse = await fetch(`http://localhost:8080/api/v1/proyect/getById/${projectId}`, {
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        });
        console.log('Project response:',await projectResponse.json());
        if (projectResponse.ok) {
            const projectData = await projectResponse.json();
            formData.projectName = projectData.nameProyect;
            formData.idProject = projectData.idProyecto;
            formData.elaborationDate = projectData.startDate;
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
                            formData.introduction= param.content;
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

                        case 'roles': {
                            const requirements = parseRoles(param.content);
                            roles = [...requirements];
                            console.log('Parsed Requirements:', roles);
                            break;
                        }

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

const parseRoles = (content) => {
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
  }

.page {
  background-color: white;
  font-family: Arial, sans-serif;
  color: #000;
  text-align: center;
  margin-top: 70px;
  padding: 20px;
}


.content {
  margin-top: 20px;
  text-align: left;
}

.textarea {
  width: 100%;
  min-height: 100px;
  margin-top: 20px;
  border: 2px solid #008080;
  border-radius: 5px;
  padding: 10px;
}

.roles-table {
  width: 100%;
  border-collapse: collapse;
  margin: 20px 0;
}

.roles-table th,
.roles-table td {
  border: 1px solid #ccc;
  padding: 10px;
  text-align: left;
}

.input-field {
  color: #000;
  width: 100%;
  padding: 5px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.add-button,
.delete-button {
  background-color: #008080;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
}

.form-container {
  max-width: 800px;
  margin: 2rem auto;
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.form-section {
  border-bottom: 1px solid #eee;
  padding-bottom: 2rem;
  margin-bottom: 2rem;
  color: #000000;
}

.section-title {
  font-size: 1.25rem;
  color: #000000;
  margin-bottom: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  margin-bottom: 1.5rem;
}

.form-input {
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  transition: border-color 0.3s;
  color: #000000;
}

.form-input:focus {
  outline: none;
  border-color: #00b8b0;
  box-shadow: 0 0 0 2px rgba(0, 184, 176, 0.1);
}

.form-textarea {
  resize: vertical;
  min-height: 100px;
}

.form-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
  margin-top: 2rem;
  padding-top: 2rem;
  border-top: 1px solid #eee;
  color:#000;
}

.interact-button,
.submit-button,
.cancel-button {
    padding: 0.75rem 1.5rem;
    border-radius: 4px;
    font-size: 1rem;
    cursor: pointer;
    transition: all 0.3s;
}

.submit-button {
    background-color: #00B8B0;
    color: white;
    border: none;
}

.submit-button:hover:not(:disabled) {
    background-color: #009B94;
}

.submit-button:disabled {
    opacity: 0.7;
    cursor: not-allowed;
}

.cancel-button {
    background-color: red;
    color: white;
    border: 1px solid #ddd;
}

.cancel-button:hover {
    background-color: #eee;
}

.interact-button {
    background-color: #009B94;
    color: white;
    border: 1px solid #ddd;
}

.interact-button:hover {
    background-color: #eee;
}

.error-message {
    margin-top: 1rem;
    padding: 1rem;
    border-radius: 4px;
    background-color: #ffe6e6;
    color: #dc3545;
    text-align: center;
}

.form-actions {
    display: flex;
    gap: 1rem;
    justify-content: flex-end;
    margin-top: 2rem;
    padding-top: 2rem;
    border-top: 1px solid #eee;
}

.save-button {
    padding: 0.75rem 1.5rem;
    border-radius: 4px;
    font-size: 1rem;
    cursor: pointer;
    transition: all 0.3s;
    background-color: #4CAF50;
    color: white;
    border: none;
}

.save-button:hover:not(:disabled) {
    background-color: #45a049;
}

.save-button:disabled {
    opacity: 0.7;
    cursor: not-allowed;
}

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
    color: black;
    padding: 2rem;
    border-radius: 8px;
    max-width: 500px;
    width: 90%;
}

.modal-content h3 {
    margin-top: 0;
    margin-bottom: 1rem;
}

.modal-actions {
    display: flex;
    justify-content: flex-end;
    gap: 1rem;
    margin-top: 2rem;
}

.confirm-button {
    padding: 0.75rem 1.5rem;
    border-radius: 4px;
    font-size: 1rem;
    cursor: pointer;
    transition: all 0.3s;
    background-color: green;
    color: white;
    border: none;
}

.confirm-button:hover {
    background-color: white;
}

.pdf-container {
    margin-top: 2rem;
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    padding: 1.5rem;
}

.pdf-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 1rem;
    padding-bottom: 1rem;
    border-bottom: 1px solid #eee;
}

.pdf-title {
    font-size: 1.25rem;
    color: #333;
    margin: 0;
}

.pdf-actions {
    display: flex;
    gap: 1rem;
}

.pdf-viewer {
    background: #f5f5f5;
    padding: 1rem;
    border-radius: 4px;
    margin-bottom: 1rem;
}

.pdf-iframe {
    width: 100%;
    height: 700px;
    border: none;
    border-radius: 4px;
}

.pdf-footer {
    display: flex;
    justify-content: flex-end;
    gap: 1rem;
    padding-top: 1rem;
    border-top: 1px solid #eee;
}

.pdf-button {
    padding: 0.75rem 1.5rem;
    border-radius: 4px;
    font-size: 1rem;
    cursor: pointer;
    transition: all 0.3s;
    display: flex;
    align-items: center;
    gap: 0.5rem;
}

.download-button {
    background-color: #00B8B0;
    color: white;
    text-decoration: none;
    border: none;
}

.download-button:hover {
    background-color: #009B94;
}

.return-button {
    background-color: #f5f5f5;
    color: #666;
    border: 1px solid #ddd;
}

.return-button:hover {
    background-color: #eee;
}

.button-icon {
    font-size: 1.2rem;
}
.invalid-input {
    border-color: #dc3545;
    background-color: #fff8f8;
}

.invalid-input:focus {
    border-color: #dc3545;
    box-shadow: 0 0 0 2px rgba(220, 53, 69, 0.1);
}

.invalid-section {
    border: 1px solid #dc3545;
    padding: 0.5rem;
    border-radius: 4px;
    background-color: #fff8f8;
}

.form-input.invalid-input::placeholder {
    color: #dc3545;
}

/* Mensaje de error para campos inválidos */
.invalid-feedback {
    color: #dc3545;
    font-size: 0.875rem;
    margin-top: 0.25rem;
}
</style>