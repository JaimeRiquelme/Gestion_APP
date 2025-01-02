<template>
  <div class="page-container">
    <div class="main-content">
      <div class="title-content">
        <h1 class="title-content-text">Plan de Gestión del Alcance</h1>
      </div>

      <div class="form-container">
        <form class="constitution-form" @submit.prevent="handleGeneratePDF">
          <!-- Información Básica -->
          <section class="form-section">
            <h2 class="section-title">Información Básica</h2>
            <div class="form-group">
              <label for="projectName">Nombre del Proyecto *</label>
              <input
                id="projectName"
                type="text"
                v-model="formData.projectName"
                class="form-input"
                placeholder="Ejemplo de Proyecto"
                required
              />
            </div>
            <div class="form-group">
              <label for="idProject">ID del Proyecto *</label>
              <input
                id="idProject"
                type="text"
                v-model="formData.idProject"
                class="form-input"
                placeholder="1"
                required
              />
            </div>
            <div class="form-group">
              <label for="productLeader">Líder del Producto</label>
              <input
                id="productLeader"
                type="text"
                v-model="formData.productLeader"
                class="form-input"
                placeholder="Nombre del líder del producto"
              />
            </div>
            <div class="form-group">
              <label for="projectLeader">Líder del Proyecto</label>
              <input
                id="projectLeader"
                type="number"
                v-model="formData.projectLeader"
                class="form-input"
                placeholder="ID del líder del proyecto"
              />
            </div>
            <div class="form-group">
              <label for="elaborationDate">Fecha de Elaboración</label>
              <input
                id="elaborationDate"
                type="date"
                v-model="formData.elaborationDate"
                class="form-input"
              />
            </div>
          </section>

          <!-- Objetivos -->
          <section class="form-section">
            <h2 class="section-title">Objetivos</h2>
            <div class="form-group">
              <label for="descriptionObjectives">Descripción de Objetivos *</label>
              <textarea
                id="descriptionObjectives"
                v-model="formData.descriptionObjectives"
                class="form-input form-textarea"
                placeholder="Describa los objetivos del proyecto"
                required
              ></textarea>
            </div>
            <div class="form-group">
              <label for="objectivesSpecific">Objetivos Específicos *</label>
              <textarea
                id="objectivesSpecific"
                v-model="formData.objectivesSpecific"
                class="form-input form-textarea"
                placeholder="Describa los objetivos específicos"
                required
              ></textarea>
            </div>
          </section>

          <!-- Alcance -->
          <section class="form-section">
            <h2 class="section-title">Alcance</h2>
            <div
              v-for="(field, index) in scopeFields"
              :key="index"
              class="form-group"
            >
              <label :for="field.name">{{ field.label }}</label>
              <textarea
                :id="field.name"
                v-model="formData[field.name]"
                class="form-input form-textarea"
                :placeholder="field.placeholder"
              ></textarea>
            </div>
          </section>

          <!-- Botones -->
          <div class="form-actions">
            <button type="button" class="cancel-button" @click="showCancelConfirmation = true">
              Cancelar
            </button>
            <button type="button" class="save-button" @click="showSaveConfirmation = true">
              Guardar
            </button>
            <button type="submit" class="generate-button">Generar Archivo</button>
          </div>
        </form>
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
            <button class="confirm-button" @click="confirmCancel">
              Sí, cancelar
            </button>
          </div>
        </div>
      </div>

      <!-- Diálogo de confirmación para guardar -->
      <div v-if="showSaveConfirmation" class="modal-overlay">
        <div class="modal-content">
          <h3>Confirmar Guardado</h3>
          <p>¿Estás seguro que deseas guardar los cambios actuales?</p>
          <div class="modal-actions">
            <button class="cancel-button" @click="showSaveConfirmation = false">
              No, seguir editando
            </button>
            <button class="save-button" @click="confirmSave">
              Sí, guardar
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      formData: {
        projectName: "",
        idProject: "",
        productLeader: "",
        projectLeader: null,
        elaborationDate: "",
        descriptionObjectives: "",
        objectivesSpecific: "",
        scopeApproach: "",
        scopePlanObjectives: "",
        scopeDefinition: "",
        scopeExclusions: "",
        scopeLimitations: "",
        scopeStatement: "",
        scopeOutputs: "",
        projectAssumptions: "",
        requirementGathering: "",
        requirementsGatheringStrategy: "",
        requirementsAnalysis: "",
        requirementsDocumentation: "",
        verificationAndControlOfScope: "",
        scopeApproval: "",
        stakeholdersApproval: "",
        changeControlSystem: "",
        edtApproval: "",
      },
      scopeFields: [
        { name: "scopeApproach", label: "Enfoque del Alcance", placeholder: "Explique el enfoque del alcance" },
        { name: "scopePlanObjectives", label: "Objetivos del Plan de Gestión del Alcance", placeholder: "Describa los objetivos del plan" },
        { name: "scopeDefinition", label: "Definición del Alcance", placeholder: "Explique cómo se define el alcance" },
        { name: "scopeExclusions", label: "Exclusiones del Alcance", placeholder: "Describa las exclusiones del alcance" },
        { name: "scopeLimitations", label: "Limitaciones del Alcance", placeholder: "Describa las limitaciones del alcance" },
        { name: "scopeStatement", label: "Enunciado del Alcance", placeholder: "Indique un enunciado general del alcance" },
        { name: "scopeOutputs", label: "Entregables del Proyecto", placeholder: "Defina los entregables del proyecto" },
        { name: "projectAssumptions", label: "Suposiciones del Proyecto", placeholder: "Describa las suposiciones consideradas" },
        { name: "requirementGathering", label: "Recopilación de Requisitos", placeholder: "Explique el proceso de recopilación de requisitos" },
        { name: "requirementsGatheringStrategy", label: "Estrategias de Recopilación de Requisitos", placeholder: "Indique las estrategias utilizadas" },
        { name: "requirementsAnalysis", label: "Análisis de Requisitos", placeholder: "Explique cómo se analizan los requisitos" },
        { name: "requirementsDocumentation", label: "Documentación de Requisitos", placeholder: "Describa cómo se documentan los requisitos" },
        { name: "verificationAndControlOfScope", label: "Verificación y Control del Alcance", placeholder: "Explique el proceso de verificación y control" },
        { name: "scopeApproval", label: "Aprobación del Alcance", placeholder: "Describa los criterios de aprobación" },
        { name: "stakeholdersApproval", label: "Aceptación Formal de los Interesados", placeholder: "Explique el procedimiento para aceptación" },
        { name: "changeControlSystem", label: "Sistema de Control de Cambios", placeholder: "Describa cómo se controlan los cambios" },
        { name: "edtApproval", label: "Aprobación Formal del EDT", placeholder: "Indique los criterios de aprobación del EDT" },
      ],
      showCancelConfirmation: false,
      showSaveConfirmation: false,
    };
  },
  methods: {
    confirmCancel() {
      Object.keys(this.formData).forEach((key) => (this.formData[key] = ""));
      this.showCancelConfirmation = false;
    },
    async confirmSave() {
      try {
        const response = await fetch("/api/documents/scope-management-plan/save", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(this.formData),
        });
        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(errorText);
        }
        alert("Información guardada correctamente.");
      } catch (error) {
        alert(`Error al guardar los datos: ${error.message}`);
      } finally {
        this.showSaveConfirmation = false;
      }
    },
    async handleGeneratePDF() {
      try {
        const response = await fetch(
          "/api/documents/scope-management-plan/generate?idExit=1",
          {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify(this.formData),
          }
        );
        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(errorText);
        }
        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = url;
        a.download = `Plan_Gestion_Alcance_${Date.now()}.pdf`;
        a.click();
        window.URL.revokeObjectURL(url);
      } catch (error) {
        alert(`Error al generar el PDF: ${error.message}`);
      }
    },
  },
};
</script>

<style scoped>
.page-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.main-content {
  padding: 2rem;
}

.title-content {
  background-color: #f5f5f5;
  padding: 1.5rem 3rem;
}

.title-content-text {
  max-width: 800px;
  margin: 0 auto;
  color: #000000;
  font-size: 1.5rem;
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
}

.cancel-button {
  background-color: #f5f5f5;
  color: #666;
  border: 1px solid #ddd;
  padding: 0.75rem 1.5rem;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
}

.cancel-button:hover {
  background-color: #eee;
}

.save-button {
  background-color: #218838;
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
}

.save-button:hover {
  background-color: #218838;
}

.generate-button {
  background-color: #00b8b0;
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
}

.generate-button:hover {
  background-color: #009b94;
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

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 1.5rem;
}

.confirm-button {
  background-color: #dc3545;
  color: white;
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.confirm-button:hover {
  background-color: #c82333;
}

</style>