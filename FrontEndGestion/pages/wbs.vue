<template>
  <div class="background">
    <v-container class="container" fluid>
      <v-row class="mb-6">
        <v-col>
          <h1 class="form-title">Estructura de Desglose del Trabajo</h1>
          <p class="text-body-1 text-grey-darken-1">
            La EDT y su diccionario son elementos clave para la gestión efectiva del alcance. Esta herramienta permite separar las fases de un proyecto en tareas más manejables.
          </p>
        </v-col>
      </v-row>

      <form class="form" @submit.prevent="handleSubmit">
        <section class="form-section">
          <h2 class="section-title">Información Básica</h2>

          <div class="form-group">
            <label>Nombre del Proyecto *</label>
            <input v-model="formData.proyectName" class="form-input" type="text" placeholder="Ingrese el nombre del proyecto" required disabled />
          </div>
      
          <div class="flex-row">
            <div class="form-group half-width">
              <label>Identificador del Proyecto *</label>
              <input v-model="formData.idProyect" class="form-input" type="text" placeholder="Ingrese el identificador del proyecto" required disabled />
            </div>
        
            <div class="form-group half-width">
              <label>Fecha de Elaboración *</label>
              <input v-model="formData.elaborationDate" class="form-input" type="date" placeholder="yyyy-mm-dd" required />
            </div>
          </div>
          
          <div class="form-group half-width">
            <label>Líder del Proyecto *</label>
            <input v-model="formData.proyectLeader" class="form-input" type="text" placeholder="Ingrese el líder del proyecto" required disabled />
          </div>

          <div class="form-group">
            <label>Promotor del Proyecto</label>
            <input v-model="formData.proyectPromotor" class="form-input" type="text" placeholder="Ingrese el promotor del proyecto" required />
          </div>

          <div class="form-group">
            <label>Declaración del alcance del proyecto</label>
            <input v-model="formData.projectScopeStatement" class="form-input" type="text" placeholder="Ingrese una descripcion del alcance del proyecto" required />
          </div>
          <h2 class="section-title">Diagrama EDT</h2>
        </section>
      </form>
  
      <v-row class="mb-6">
        <v-col cols="12" md="8" class="mx-auto">
          <div class="wbs-tree">
            <div class="wbs-node level-0">
              <div class="d-flex align-center">
                <v-text-field
                  v-model="formData.proyectName"
                  variant="outlined"
                  density="compact"
                  bg-color="grey-lighten-2"
                  class="wbs-input"
                  placeholder="Nombre del proyecto"
                  disabled
                ></v-text-field>
                <div class="ms-2 button-container">
                  <v-btn
                    icon
                    size="small"
                    variant="text"
                    @click="addPiece(null)"
                  >
                    <v-icon color="blue-darken-3">mdi-plus-circle</v-icon>
                  </v-btn>
                </div>
              </div>
            </div>
            <TransitionGroup name="list" tag="div">
              <WBSNode
                v-for="piece in topLevelPieces"
                :key="piece.id"
                :piece="piece"
                :all-pieces="wbsPieces"
                @add-piece="addPiece"
                @delete-piece="deletePiece"
                @update-piece="updatePiece"
              />
            </TransitionGroup>
          </div>
        </v-col>
      </v-row>
  
      <v-row>
        <v-col>
          <v-table class="rounded-table":headers="headers" :items="items" :items-per-page="5">
            <thead>
              <TransitionGroup name="list">
                <tr>
                  <th>Nivel</th>
                  <th>Código EDT</th>
                  <th>Nombre</th>
                  <th>Descripción</th>
                </tr>
              </TransitionGroup>
            </thead>
            <tbody>
              <TransitionGroup name="list">
                <tr v-for="item in wbsPieceDictionary" :key="item.id">
                  <td>{{ item.level }}</td>
                  <td>{{ item.code }}</td>
                  <td>{{ item.name }}</td>
                  <td>
                    <v-textarea
                      class="table-textarea"
                      v-if="item.level >= 2"
                      v-model="item.description"
                      variant="outlined"
                      rows="1"
                      hide-details
                      @input="validateNoSpecialSymbol($event, 'wbsPieceDictionary', item)"
                      @update:model-value="updatePieceDescription(item.id, $event)"
                    ></v-textarea>
                    <span v-if="fieldHadSpecialSymbol('wbsPieceDictionary', item.id)" class="invalid-feedback">No se permite el simbolo en este campo</span>
                  </td>
                </tr>
              </TransitionGroup>
            </tbody>
          </v-table>
        </v-col>
      </v-row>

      <div class="form-actions">
          <button type="button" class="cancel-button" @click="showCancelConfirmation = true">
              Cancelar
          </button>
          <button type="button" class="save-button" :disabled="loading" @click="showSaveConfirmation = true">
              {{ loading ? 'Guardando...' : 'Guardar' }}
          </button>
          <button type="submit" class="submit-button" :disabled="loading" @click="handleSubmit">
              {{ loading ? 'Creando...' : 'Crear Documento' }}
          </button>
      </div>
      <div v-if="showCancelConfirmation" class="modal-overlay">
          <div class="modal-content">
              <h3 class="section-title">Confirmar Cancelación</h3>
              <label>¿Estás seguro que deseas cancelar? Los cambios no guardados se perderán.</label>
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
      <div v-if="showSaveConfirmation" class="modal-overlay">
          <div class="modal-content">
              <h3 class="section-title">Confirmar Guardado</h3>
              <label>¿Estás seguro que deseas guardar los cambios actuales?</label>
              <div class="modal-actions">
                  <button class="cancel-button" @click="showSaveConfirmation = false">
                      No, seguir editando
                  </button>
                  <button class="save-button" @click="handleSave">
                      Sí, guardar
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
              <button @click="navigateTo('/ScopeManagementView')" class="pdf-button return-button">
                  Volver al Dashboard
              </button>
          </div>
      </div>
    </v-container>
  </div>
  <AlertPopup
    :show="alert.show"
    :title="alert.title"
    :message="alert.message"
    :type="alert.type"
    @confirm="handleAlertConfirm"
  />
</template>

<script setup>
import { ref, computed } from 'vue'
import { useAuthStore } from '../stores/auth';
import { useProjectStore } from '../stores/project';
import { useProcessStore } from '../stores/Process';
import { useExitStore } from '../stores/Exit';

const projectName = ref('Nombre del proyecto')
const wbsPieces = ref([])
const nextId = ref(1)

const AuthStore = useAuthStore();
const ProjectStore = useProjectStore();
const ProcessStore = useProcessStore();
const ExitStore = useExitStore();

const loading = ref(false);
const errorMessage = ref('');
const pdfUrl = ref(null);
const showCancelConfirmation = ref(false);
const showSaveConfirmation = ref(false);
const { fetch } = useFetchWithAuth();

const tableFieldsWithSpecialSymbol = ref(new Map());

const url_doc_generator = "http://localhost:8080/api/documents/ScopeBaseline"
const url_exit = "http://localhost:8080/api/v1/exit"
const url_parameters = "http://localhost:8080/api/v1/parameters"

const formData = reactive({
    proyectName: "",
    idProyect: "",
    company: "",
    proyectPromotor: "",
    proyectLeader: "",
    elaborationDate: new Date().toISOString().split('T')[0],
    projectScopeStatement: "",
    edtWbs: ""
  });

const topLevelPieces = computed(() => {
  return wbsPieces.value.filter(piece => piece.parentId === null)
})

const wbsPieceDictionary = computed(() => {
  const items = [{
    id: 0,
    level: 0,
    code: '0',
    parentId: -1,
    name: formData.proyectName,
    description: '',
    childId: 0
  }]

  function addPieceToItems(piece, parentCode = '') {
    const code = parentCode ? `${parentCode}.${piece.childId}` : `${piece.childId}`
    items.push({
      id: piece.id,
      level: piece.level,
      code,
      parentId: piece.parentId,
      name: piece.name,
      description: piece.description,
      childId: piece.childId
    })

    const children = wbsPieces.value.filter(p => p.parentId === piece.id)
    children.forEach(child => addPieceToItems(child, code))
  }

  topLevelPieces.value.forEach(piece => addPieceToItems(piece))
  return items
})


function addPiece(parentId) {
  const parentChildren = wbsPieces.value.filter(piece => piece.parentId === parentId);
  const newChildId = parentChildren.length + 1; // New childId based on existing children

  const newPiece = {
    id: nextId.value++,
    parentId,
    level: parentId === null ? 1 : wbsPieces.value.find(p => p.id === parentId).level + 1,
    code: '',
    name: '',
    description: '',
    childId: newChildId, // Assign childId
  };

  if (newPiece.level <= 3) {
    const lastChildIndex = wbsPieces.value.reduce((lastIndex, piece, index) => {
      if (piece.parentId === parentId && index > lastIndex) {
        return index;
      }
      return lastIndex;
    }, -1);

    const insertIndex = lastChildIndex !== -1 ? lastChildIndex + 1 : wbsPieces.value.length;
    wbsPieces.value.splice(insertIndex, 0, newPiece);
  }
}


function deletePiece(pieceId) {
  const deleteRecursive = (id) => {
    const index = wbsPieces.value.findIndex(p => p.id === id);
    if (index !== -1) {
      cleanMapList('wbsPieceDictionary')
      wbsPieces.value.splice(index, 1);
      wbsPieces.value.filter(p => p.parentId === id).forEach(child => deleteRecursive(child.id));
    }
  };

  const piece = wbsPieces.value.find(p => p.id === pieceId);
  if (piece) {
    const parent = wbsPieces.value.find(p => p.id === piece.parentId);
    const parentCode = parent ? parent.code : null;

    // Delete the selected piece and its descendants
    deleteRecursive(pieceId);

    // Rearrange childId and update codes for remaining siblings
    const siblings = wbsPieces.value.filter(p => p.parentId === piece.parentId);
    siblings.forEach((sibling, index) => {
      sibling.childId = index + 1;
      sibling.code = parentCode ? `${parentCode}.${sibling.childId}` : `${sibling.childId}`;
    });
  }
}


function updatePiece(pieceId, newName) {
  const pieceIndex = wbsPieces.value.findIndex(p => p.id === pieceId);
  if (pieceIndex !== -1) {
    wbsPieces.value[pieceIndex] = {
      ...wbsPieces.value[pieceIndex],
      name: newName,
    };
  }
}


function updatePieceDescription(pieceId, newDescription) {
  const piece = wbsPieces.value.find(p => p.id === pieceId)
  if (piece) {
    piece.description = newDescription
  }
}

function formatWBS(){
  const fases = wbsPieceDictionary.value.filter(piece => piece.level === 1);

  const result = fases.map(fase => {
    const tareas = wbsPieceDictionary.value.filter(piece => piece.parentId === fase.id && piece.level === 2);

    const tareasString = tareas
      .map(tarea => `${tarea.name || 'Tarea sin nombre'}; ${tarea.description || 'Sin descripción'}`)
      .join(', ');

    return `${fase.name || 'Fase sin nombre'}{${tareasString}}`;
  });

  return result.join('|');
}

function parseWBSSyntaxString(wbsString) {
  // Clear the wbsPieces array (or initialize it if empty)
  wbsPieces.value = [];

  const fases = wbsString.split('|'); // Split by the "|" separator to get each "Fase"

  fases.forEach((faseString, faseIndex) => {
    const faseMatch = faseString.match(/^([^{}]+){([^}]*)}$/);

    if (!faseMatch) {
      console.error(`Invalid Fase format: ${faseString}`);
      return;
    }

    const faseName = faseMatch[1].trim(); // Get Fase name
    const tareasString = faseMatch[2]; // Get Tareas list

    // Add the Fase to the wbsPieces array
    const fase = {
      id: nextId.value++,
      level: 1,
      name: faseName,
      description: '',
      parentId: null,
      childId: faseIndex + 1
    };

    wbsPieces.value.push(fase);

    // Split Tareas by comma and process each Tarea
    const tareas = tareasString.split(',').map(tareaString => tareaString.trim());

    tareas.forEach((tareaString, tareaIndex) => {
      const tareaMatch = tareaString.match(/^([^;]+);(.*)$/);

      if (!tareaMatch) {
        console.error(`Invalid Tarea format: ${tareaString}`);
        return;
      }

      const tareaName = tareaMatch[1].trim();
      const tareaDescription = tareaMatch[2].trim();

      // Add the Tarea to the wbsPieces array
      const tarea = {
        id: nextId.value++,
        level: 2,
        name: tareaName,
        description: tareaDescription,
        parentId: fase.id,
        childId: tareaIndex + 1
      };

      wbsPieces.value.push(tarea);
    });
  });

  console.log("Parsed WBS Pieces:", wbsPieces.value);
}

function validateWBSStructure() {
  // Check if there is at least one level 1 piece (Fase)
  const fases = wbsPieces.value.filter(piece => piece.level === 1);

  if (fases.length === 0) {
    errorMessage.value = "El EDT debe tener al menos una fase";
    return false;
  }

  // Check if each Fase has at least one Tarea
  for (const fase of fases) {
    const tareas = wbsPieces.value.filter(piece => piece.parentId === fase.id && piece.level === 2);
    if (tareas.length === 0) {
      errorMessage.value = "Cada fase del EDT debe tener al menos una tarea";
      return false;
    }
  }

  // If all validations pass
  return true;
}

const validateForm = () => {
    errorMessage.value = ''

    // Form data validation
    Object.entries(formData).forEach(([key, value]) => {
        if (typeof value === 'string' && !value.trim()) {
          if (key != 'edtWbs') {
            errorMessage.value = 'Por favor, rellene todos los campos';
            return false;
          };
        };
    });

    // Escape if error occurs
    if (errorMessage.value != '') {
      return false;
    }

    // Special Symbol validation and empty validation
    wbsPieces.value.forEach(p => {
      if (!p.name.trim()) {
        errorMessage.value = 'Todas las Fases y Tareas del EDT deben tener nombres';
        return false;
      }
      if(hasSpecialSymbol(p.name) || hasSpecialSymbol(p.description)){
        errorMessage.value = 'Los nombres y descripciones de Tareas o Fases no pueden contener los simbolos: | { } , ;'
        return false;
      }
    })

    // Escape if error occurs
    if (errorMessage.value != '') {
      return false;
    }

    // Verify EDT structure
    if (!validateWBSStructure()){
      return false;
    }

    console.log("Validation succeeded");
    return true;
};

//----- SPECIAL SYMBOL VALIDATION AND ERROR MESSAGE CONTROL -----
const hasSpecialSymbol = (text) => {
  const forbiddenCharacters = /[|{},;]/g;
  return forbiddenCharacters.test(text);
};
  
const validateNoSpecialSymbol = (event, type, item) => {
    const forbiddenCharacters = /[|{},;]/g;
    const fieldValue = event.target.value;
    if (hasSpecialSymbol(fieldValue)) {

      if (type === 'wbsPieceDictionary') {
        addItemToListInMap('wbsPieceDictionary', item.id);
        const piece = wbsPieces.value.find(p => p.id === item.id);
        piece.description = item.description.replace(forbiddenCharacters, '');
      }
    } else {
      if (type === 'wbsPieceDictionary') {
        removeItemFromMapList('wbsPieceDictionary', item.id);
      }
    }
};

const addItemToListInMap = (key, item) => {
  if (tableFieldsWithSpecialSymbol.value.has(key)) {
    if (! tableFieldsWithSpecialSymbol.value.get(key).includes(item)){
      tableFieldsWithSpecialSymbol.value.get(key).push(item);
    }
  } else {
    tableFieldsWithSpecialSymbol.value.set(key, [item]);
  }
};

const removeItemFromMapList = (key, item) => {
  if (tableFieldsWithSpecialSymbol.value.has(key)) {
    const list = tableFieldsWithSpecialSymbol.value.get(key);
    const index = list.indexOf(item);
    if (index > -1) {
      list.splice(index, 1);
    }
  }
};

const cleanMapList = (key) => {
  tableFieldsWithSpecialSymbol.value.set(key, []);
}

const fieldHadSpecialSymbol = (key, item) => {
  if (tableFieldsWithSpecialSymbol.value.has(key)) {
    return tableFieldsWithSpecialSymbol.value.get(key).includes(item)
  }
};

//----- END OF COMMA VALIDATION AND ERROR MESSAGE CONTROL -----

const fetchParameterData = async () => {
  try {
    const userId = AuthStore.userId;
    const token = AuthStore.token;
    const projectId = ProjectStore.projectId;
    if (!userId || !token || !projectId) {
      showAlert('Error de Sesión', '¡Sesión no iniciada o proyecto no seleccionado!', 'error');
      return;
    }

    await getOrGenerateExit();
    const exitId = ExitStore.exitId;
    if (exitId) {
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
                    case 'proyectPromotor':
                        formData.proyectPromotor = param.content;
                        break;
                    case 'elaborationDate':
                        formData.elaborationDate = param.content;
                        break;
                    case 'projectScopeStatement':
                        formData.projectScopeStatement = param.content;
                        break;
                    case 'edtWbs':
                        parseWBSSyntaxString(param.content);
                        break;
                    default:
                        console.log(`Parámetro no manejado: ${param.nameParameter}`);
                        break;
                }
            });
        }
    }
  } catch {
    throw new Error('Error en la obtencion de Parametros');
  }
}

const fetchProjectData = async () => {
  try {
      loading.value = true;
      errorMessage.value = '';
      const userId = AuthStore.userId;
      const token = AuthStore.token;
      const projectId = ProjectStore.projectId;
      if (!userId || !token || !projectId) {
        showAlert('Error de Sesión', '¡Sesión no iniciada o proyecto no seleccionado!', 'error');
        return;
      }
      // Obtain proyect data
      const respondeProyect = await fetch(`http://localhost:8080/api/v1/proyect/getById/${projectId}`, {
          method: 'GET',
          headers: {
              'Authorization': `Bearer ${token}`,
              'Content-Type': 'application/json',
          }
      });
      // Obtain user data
      const respondeUser = await fetch(`http://localhost:8080/api/v1/user/getById/${userId}`, {
          method: 'GET',
          headers: {
              'Authorization': `Bearer ${token}`,
              'Content-Type': 'application/json',
          }
      });
      if (!respondeProyect.ok || !respondeUser.ok) {
          throw new Error('Error al obtener la información necesaria');
      }
      const projectDataResponse = await respondeProyect.json();
      const userDataResponse = await respondeUser.json();
      // Updating form data
      formData.proyectName = projectDataResponse.nameProyect;
      formData.idProyect = projectDataResponse.idProyecto;
      formData.company = projectDataResponse.organization;
      formData.proyectLeader = `${userDataResponse.names} ${userDataResponse.secondNames}`;
      formData.elaborationDate = projectDataResponse.startDate;

      await fetchParameterData();
  } catch (error) {
      console.error('Error fetching data:', error);
      errorMessage.value = 'Error al obtener la información. Por favor, intenta nuevamente.';
  } finally {
      loading.value = false;
  }
};

const saveData = async () => {
    try {
        const userId = AuthStore.userId;
        const token = AuthStore.token;
        const projectId = ProjectStore.projectId;

        if (!userId || !token || !projectId) {
            showAlert('Error de Sesión', '¡Sesión no iniciada!, redirigiendo a login...', 'error');
            return;
        }

        await getOrGenerateExit(); // Puts exitId in ExitStore

        // Formatting WBS data
        const formattedWBS = formatWBS();

        const saveData = await fetch(url_parameters + `/saveParametersList?idExit=${ExitStore.exitId}`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                ...formData,
                edtWbs: formattedWBS,
            }),
        }); 

        if (!saveData.ok) {
            const errorData = await saveData.json();
            throw new Error(errorData.message || 'Error al guardar los datos');
        }

        const responseText = await saveData.text();

        // If JSON empty but status is ok, then it's a success
        if (saveData.ok && (!responseText || responseText.trim() === '')) {
            return { success: true, message: 'Datos guardados correctamente' };
        }

        try {
            const responseData = JSON.parse(responseText);
            return responseData;
        } catch (e) {
            // If JSON invalid but status is ok, then it's a success
            if (saveData.ok) {
                return { success: true, message: 'Datos guardados correctamente' };
            }
            throw new Error('Respuesta inválida del servidor');
        }
    } catch (error) {
        showAlert('Error', 'Error al guardar los datos. Por favor, intenta nuevamente.', 'error');
        throw error;
    }
};

const getExistingExit = async () => {
  const nameExit = 'EDT';
  const idProcess = ProcessStore.processId;
  if (!idProcess) {
      showAlert('Error de Sesión', 'Proceso no pudo ser identificado, redirigiendo a vista principal...', 'error');
      navigateTo('/principalView');
      return;
  }

  const checkResponse = await fetch(
      url_exit + `/getByIdProcessAndNameExit?idProcess=${idProcess}&nameExit=${nameExit}`,
      {
          headers: {
              'Authorization': `Bearer ${AuthStore.token}`,
              'Content-Type': 'application/json',
          },
      }
  );

  if (checkResponse.ok) {
    const responseBody = await checkResponse.text()
    if (responseBody.trim()) {
      const exitData = JSON.parse(responseBody);
      ExitStore.exitId = exitData.idExit;
      ExitStore.exitName = exitData.nameExit;
      return true;
    }
  } 
  return false;
}

const createNewExit = async () => {
  const idProcess = ProcessStore.processId;
  if (!idProcess) {
      showAlert('Error de Sesión', 'Proceso no pudo ser identificado, redirigiendo a vista principal...', 'error');
      navigateTo('/principalView');
      return;
  }
  const createResponse = await fetch(
      url_exit + '/create',
      {
          method: 'POST',
          headers: {
              'Authorization': `Bearer ${AuthStore.token}`,
              'Content-Type': 'application/json',
          },
          body: JSON.stringify({
              idProcess: ProcessStore.processId,
              nameExit: "EDT",
              state: "Activo",
              dateCreation: formData.elaborationDate,
              dateValidation: new Date().toISOString().split('T')[0],
              priority: "Media",
              responsible: formData.proyectLeader,
              description: "La EDT (Estructura de Desglose del Trabajo) es una herramienta comúnmente ocupada en la Gestión del Alcance"
          })
      }
  );
  const responseText = await createResponse.text();
  if (!responseText.trim()) {
      throw new Error('No se recibió ningún contenido JSON');
  }
  if (!createResponse.ok) {
      throw new Error(`Error al crear la salida: ${responseText}`);
  }

  const newExitData = JSON.parse(responseText);
  ExitStore.exitId = newExitData.idExit;
  ExitStore.exitName = newExitData.nameExit;
}

const getOrGenerateExit = async () => {
  try {
    if (!await getExistingExit()) {
      await createNewExit();
    }
  } catch (error) {
    console.error('Error en la gestión del Exit:', error);
  }
}

onMounted(() => {
  fetchProjectData();
});

// Alert popup handling START
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
      navigateTo('/login');
    }
};
// Alert popoup handling END


// Handlers START
const handleCancel = () => {
  showCancelConfirmation.value = false;
  navigateTo('/principalView');
};


const handleSave = async () => {
    try {
        loading.value = true;
        showSaveConfirmation.value = false;

        await saveData();
        showAlert('Éxito', 'Los datos se han guardado correctamente', 'success');

    } catch (error) {
        showAlert('Error', 'Error al guardar los datos. Por favor, intenta nuevamente.', 'error');
    } finally {
        loading.value = false;
    }
};


const handleSubmit = async () => {
  if (!validateForm()) {
      return;
  }
  try {
      loading.value = true;
      errorMessage.value = '';
      const userId = AuthStore.userId;
      const token = AuthStore.token;
      const projectId = ProjectStore.projectId;
      if (!userId || !token || !projectId) {
          showAlert('Error de Sesión', '¡Sesión no iniciada!, redirigiendo a login...', 'error');
          return;
      }

      // Getting Exit Id
      await getOrGenerateExit();
      const exitId = ExitStore.exitId;

      const formattedWBS = formatWBS();

      const responseWBS = await fetch(url_doc_generator + `/generate?idExit=${exitId}`, {
          method: 'POST',
          headers: {
              'Authorization': `Bearer ${token}`,
              'Content-Type': 'application/json',
          },
          body: JSON.stringify({
              ...formData,
              edtWbs: formattedWBS,
          }),
      });
      if (!responseWBS.ok) {
          const errorData = await responseWBS.json();
          throw new Error(errorData.message || 'Error al crear la Estructura de Desglose del Trabajo');
      }

      const pdfBlob = await responseWBS.blob();
      pdfUrl.value = URL.createObjectURL(pdfBlob);
  } catch (error) {
      showAlert('Error', error.message || 'Error al crear la Estructura de Desglose del Trabajo. Por favor, intenta nuevamente.', 'error');
  } finally {
      loading.value = false;
  }
};
// Handlers END

</script>

<style scoped>

.background {
  background-color: #F5F5F5;
  width: 100%;
  min-height: 100vh; /* Ensure it covers the full viewport */
  display: flex;
  align-items: center; /* Center the form vertically */
  justify-content: center; /* Center the form horizontally */
  box-sizing: border-box; /* Include padding in width/height calculations */
}

.container {
  width: 100%;
  max-width: 80%;
  margin: auto;
  background-color: #ffffff !important;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  margin-top: 150px; /* Moves the form below the header */
  margin-bottom: 5%; /* Moves the form below the header */
}

.form-title {
  text-align: center;
  margin-bottom: 20px;
  font-size: 1.8rem;
  color: #333;
}
  
.form-section {
  margin-bottom: 20px;
}
  
.section-title {
  font-size: 1.5rem;
  margin-bottom: 10px;
  margin-left: 15px;
  margin-right: 15px;
  color: #444;
}
  
.form-group {
  margin-bottom: 15px;
  margin-left: 15px;
  margin-right: 15px;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  margin-bottom: 1.5rem;
}

.flex-row {
  display: flex;
  gap: 20px;
}

.half-width {
  flex: 1;  /* Ensure each field takes half the available width */
}

input,
textarea {
  width: 100%;
  padding: 10px;
  font-size: 1rem;
  border: 1px solid #ddd;
  border-radius: 5px;
  background-color: #fff;
  color: #555;
}

.form-input {
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  transition: border-color 0.3s;
  color: #000000;
  background-color: #ffffff;
}

input[type="date"].form-input {
  color: #000000;
  background-color: #ffffff;
  cursor: pointer;
  position: relative;
  padding: 0.75rem;
}

input[type="date"]::-webkit-calendar-picker-indicator {
  opacity: 1;
  display: block;
  background: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='24' height='24' viewBox='0 0 24 24' fill='none' stroke='%23666' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Crect x='3' y='4' width='18' height='18' rx='2' ry='2'%3E%3C/rect%3E%3Cline x1='16' y1='2' x2='16' y2='6'%3E%3C/line%3E%3Cline x1='8' y1='2' x2='8' y2='6'%3E%3C/line%3E%3Cline x1='3' y1='10' x2='21' y2='10'%3E%3C/line%3E%3C/svg%3E") no-repeat center;
  width: 20px;
  height: 20px;
  cursor: pointer;
}

.form-input:focus {
  outline: none;
  border-color: #00B8B0;
  box-shadow: 0 0 0 2px rgba(0, 184, 176, 0.1);
}
.form-input:disabled {
  color: #007973;
}

.form-textarea {
  resize: vertical;
  min-height: 100px;
}

textarea {
  resize: vertical;
}

.wbs-tree {
  padding: 2rem;
}

.wbs-input {
  max-width: 300px;
}

.level-0 {
  margin-left: 0;
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

.button-container {
  position: relative;
  top: -10px;
}

/* Dictionary table animations */
.v-table {
  overflow: hidden;
}

.v-table thead tbody tr {
  transition: all 0.5s ease;
}

.list-enter-from,
.list-leave-to {
  opacity: 0;
  transform: translateX(30px);
}

.list-leave-active {
  position: absolute;
}

.rounded-table {
  border-radius: 16px;
  background-color: #5f5f5f;
  border-color: black;
  overflow: hidden; /* Ensure that the content stays within the rounded corners */
}

.rounded-table .v-table-header,
.rounded-table .v-table__wrapper {
  border-radius: 16px;
}

.table-textarea {
  margin-top: 5px;
  margin-bottom: 5px;
}

/*NEW STYLES*/

label {
  display: block;
  font-size: 1rem;
  margin-bottom: 5px;
  color: #555;
}

.section-title {
  font-size: 1.5rem;
  margin-bottom: 10px;
  margin-left: 15px;
  margin-right: 15px;
  color: #444;
}

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
    background-color: #f5f5f5;
    color: #666;
    border: 1px solid #ddd;
}

.cancel-button:hover {
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
    background-color: #dc3545;
    color: white;
    border: none;
}

.confirm-button:hover {
    background-color: #c82333;
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

.invalid-feedback {
    color: #ff7d8a;
    font-size: 0.875rem;
    margin-top: 0.25rem;
    display: block;
}
</style>

