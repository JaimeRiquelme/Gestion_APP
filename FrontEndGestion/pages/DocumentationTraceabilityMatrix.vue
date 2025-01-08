<template>
  <div class="page-container">
    <div class="form-container">
      <h1 style="color: black;"><strong>Generar Documento de trazabilidad y sus matrices</strong></h1>

      <form @submit.prevent="submitForm">
        <div class="form-group">
          <label for="idExit"><strong>ID de Salida:</strong></label>
          <input type="number" id="idExit" v-model="idExit" placeholder="Ingrese el ID de salida" required />
        </div>

        <div class="form-group">
          <label for="proyectName"><strong>Nombre del Proyecto:</strong></label>
          <input type="text" id="proyectName" v-model="formData.proyectName" placeholder="Ingrese el nombre del proyecto" required />
        </div>

        <div class="form-group">
          <label for="idProyect"><strong>ID del Proyecto:</strong></label>
          <input type="text" id="idProyect" v-model="formData.idProyect" placeholder="Ingrese el ID del proyecto" required />
        </div>

        <div class="form-group">
          <label for="proyectLeader"><strong>Líder del Proyecto:</strong></label>
          <input type="text" id="proyectLeader" v-model="formData.proyectLeader" placeholder="Ingrese el líder del proyecto" required />
        </div>

        <div class="form-group">
          <label for="qaLeader"><strong>Líder de QA:</strong></label>
          <input type="text" id="qaLeader" v-model="formData.qaLeader" placeholder="Ingrese el líder de QA" required />
        </div>

        <div class="form-group">
          <label for="elaborationDate"><strong>Fecha de Elaboración:</strong></label>
          <input type="date" id="elaborationDate" v-model="formData.elaborationDate" required />
        </div>

        <button type="button" @click="openPopup" class="submit-button"><strong>Añadir Documentación de Requisitos</strong></button>
        <button type="button" @click="openTraceabilityPopup" class="submit-button"><strong>Añadir Matriz de Trazabilidad</strong></button>
        <button type="submit" class="submit-button"><strong>Generar Documento</strong></button>
      </form>

      <!-- Botón para guardar los datos -->
      <button type="button" @click="saveData" class="submit-button"><strong>Guardar Datos</strong></button>
    </div>

    <!-- Popup para Documentación de Requisitos -->
    <div v-if="showPopup" class="popup-overlay">
      <div class="popup">
        <h2><strong>Documentación de Requisitos</strong></h2>
        <table>
          <thead>
            <tr>
              <th><strong>Identificador de Requisitos</strong></th>
              <th><strong>Descripción del Requisito</strong></th>
              <th><strong>Responsable</strong></th>
              <th><strong>Eliminar</strong></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(row, index) in documentationAndVersionControl" :key="index">
              <td><input type="text" v-model="row.idRequirement" placeholder="ID del requisito" /></td>
              <td><input type="text" v-model="row.description" placeholder="Descripción" /></td>
              <td><input type="text" v-model="row.responsible" placeholder="Responsable" /></td>
              <td>
                <button @click="removeRow(index)" class="submit-button">Eliminar</button>
              </td>
            </tr>
          </tbody>
        </table>
        <button @click="addRow" class="submit-button"><strong>Añadir Fila</strong></button>
        <button @click="closePopup" class="submit-button"><strong>Guardar y Cerrar</strong></button>
      </div>
    </div>

    <!-- Popup para Matriz de Trazabilidad -->
    <div v-if="showTraceabilityPopup" class="popup-overlay">
      <div class="popup">
        <h2><strong>Matriz de Trazabilidad de Requisitos</strong></h2>
        <table>
          <thead>
            <tr>
              <th><strong>Identificador del Requerimiento</strong></th>
              <th><strong>Sustento de Inclusión</strong></th>
              <th><strong>Fecha de Inclusión</strong></th>
              <th><strong>Propietario</strong></th>
              <th><strong>Fuente</strong></th>
              <th><strong>Prioridad</strong></th>
              <th><strong>Estado Actual</strong></th>
              <th><strong>Criterio de Aceptación</strong></th>
              <th><strong>Entregable del WSB</strong></th>
              <th><strong>Eliminar</strong></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(row, index) in requirementsTraceabilityMatrix" :key="index">
              <td><input type="text" v-model="row.idRequirement" placeholder="ID del requerimiento" /></td>
              <td><input type="text" v-model="row.sustento" placeholder="Sustento de inclusión" /></td>
              <td><input type="date" v-model="row.fechaInclusion" /></td>
              <td><input type="text" v-model="row.propietario" placeholder="Propietario" /></td>
              <td><input type="text" v-model="row.fuente" placeholder="Fuente" /></td>
              <td>
                <select v-model="row.prioridad">
                  <option value="alta">Alta</option>
                  <option value="media">Media</option>
                  <option value="baja">Baja</option>
                </select>
              </td>
              <td><input type="text" v-model="row.estadoActual" placeholder="Estado actual" /></td>
              <td><input type="text" v-model="row.criterioAceptacion" placeholder="Criterio de aceptación" /></td>
              <td><input type="text" v-model="row.entregableWSB" placeholder="Entregable del WSB" /></td>
              <td>
                <button @click="removeTraceabilityRow(index)" class="submit-button">Eliminar</button>
              </td>
            </tr>
          </tbody>
        </table>
        <button @click="addTraceabilityRow" class="submit-button"><strong>Añadir Fila</strong></button>
        <button @click="closeTraceabilityPopup" class="submit-button"><strong>Guardar y Cerrar</strong></button>
      </div>
    </div>
  </div>
</template>
  
  <script setup>
  import { ref } from 'vue';
  import { useAuthStore } from '../stores/auth';
  import { useProjectStore } from '../stores/project';
  import { useExitStore } from '../stores/Exit';

  const AuthStore = useAuthStore();
  const ProjectStore = useProjectStore();
  const { fetch } = useFetchWithAuth();
  const ExitStore = useExitStore();

  const url_parameters = "http://localhost:8080/api/v1/parameters"
  const idExit = ExitStore.exitId;
  const formData = ref({
    proyectName: ProjectStore.projectName,
    idProyect: ProjectStore.projectId,
    proyectLeader: '',
    qaLeader: '',
    elaborationDate: '',
  });
  const showPopup = ref(false);
  const showTraceabilityPopup = ref(false);
  const documentationAndVersionControl = ref([]);
  const requirementsTraceabilityMatrix = ref([]);
  
  function openPopup() {
    showPopup.value = true;
  }
  
  function closePopup() {
    showPopup.value = false;
  }
  
  function addRow() {
    documentationAndVersionControl.value.push({
      idRequirement: '',
      description: '',
      responsible: '',
    });
  }
  
  function removeRow(index) {
    documentationAndVersionControl.value.splice(index, 1);
  }
  
  function openTraceabilityPopup() {
    showTraceabilityPopup.value = true;
  }
  
  function closeTraceabilityPopup() {
    showTraceabilityPopup.value = false;
  }
  
  function addTraceabilityRow() {
    requirementsTraceabilityMatrix.value.push({
      idRequirement: '',
      sustento: '',
      fechaInclusion: '',
      propietario: '',
      fuente: '',
      prioridad: 'alta',
      estadoActual: '',
      criterioAceptacion: '',
      entregableWSB: '',
    });
  }
  
  function removeTraceabilityRow(index) {
    requirementsTraceabilityMatrix.value.splice(index, 1);
  }
  
  function formatRequirements() {
    return documentationAndVersionControl.value
      .map(
        (row, index) => `&${row.idRequirement},${row.description},${row.responsible}&${index + 1}`
      )
      .join('') + '&';
  }
  
  function formatTraceability() {
    return requirementsTraceabilityMatrix.value
      .map(
        (row, index) => `&${row.idRequirement},${row.sustento},${row.fechaInclusion},${row.propietario},${row.fuente},${row.prioridad},${row.estadoActual},${row.criterioAceptacion},${row.entregableWSB}&${index + 1}`
      )
      .join('') + '&';
  }
  
  async function submitForm() {
  try {
    // Validación de sesión y datos del usuario
    const token = AuthStore.token;

    if ( !token) {
      alert('ALERTA: ¡Sesión no iniciada! Redirigiendo a login...');
      await router.push('/login');
      return;
    }

    // Validación del ID de Salida
    if (!ExitStore.exitId) {
      alert('Por favor, ingrese un ID de Salida válido.');
      return;
    }

    // Preparar los datos del formulario
    formData.value.documentationAndVersionControl = formatRequirements();
    formData.value.requirementsTraceabilityMatrix = formatTraceability();

    // Enviar datos al backend
    const response = await fetch(
      `http://localhost:8080/api/documents/TraceabilityMatrix/generate?idExit=${ExitStore.exitId}`, 
      {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData.value), // Convertir datos a JSON
      }
    );

    if (!response.ok) {
      throw new Error(`Error al generar el documento: ${response.statusText}`);
    }

    // Procesar la respuesta (Blob para descarga)
    const blob = await response.blob();
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', 'alcance_y_matriz_de_trazabilidad.pdf');
    document.body.appendChild(link);
    link.click();
    link.remove();

    alert('Documento generado exitosamente.');
  } catch (error) {
    console.error('Error al generar el documento:', error.message || error);
    alert('Hubo un error al generar el documento. Por favor, revise los datos ingresados.');
  }
}



const saveData = async () => {
    try {
        const userId = AuthStore.userId;
        const token = AuthStore.token;
        const projectId = ProjectStore.projectId;

        if (!userId || !token || !projectId) {
            return;
        }


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
        throw error;
    }
};

  </script>
  
  <style scoped>
  .page-container {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    min-height: 100vh;
    background-color: #fff;
  }
  
  .form-container {
    background: #f9f9f9;
    padding: 30px;
    border-radius: 8px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    width: 50%;
    text-align: center;
  }
  
  .form-group {
    margin-bottom: 20px;
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  
  label {
    font-weight: bold;
    color: black;
    margin-bottom: 8px;
  }
  
  input, select {
    width: 100%;
    max-width: 300px;
    padding: 8px;
    border: 1px solid #ddd;
    border-radius: 4px;
    color: black;
  }
  
  .popup input::placeholder {
    color: black;
  }
  
  .submit-button {
    background-color: #00B8B0;
    color: black;
    padding: 10px 15px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    margin: 5px;
  }
  
  .submit-button:hover {
    background-color: #0056b3;
  }
  
  .popup-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
  }
  
  .popup {
    background: white;
    padding: 20px;
    border-radius: 8px;
    max-width: 80%;
    overflow-y: auto;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    color: black;
  }
  
  table {
    width: 100%;
    border-collapse: collapse;
    margin: 20px 0;
  }
  
  th, td {
    padding: 12px;
    text-align: left;
    border-bottom: 1px solid #ddd;
  }
  
  th {
    background-color: #f4f4f4;
  }
  </style>
  