<template>
    <div class="page-container">
        <main class="main-content">
            <div class="title-content">
                <h1 class="title-content-text">Enunciado de Alcance del Proyecto</h1>
            </div>

            <div class="form-container">
                <form @submit.prevent="handleSubmit" class="scope-form">
                    <!-- Información básica -->
                    <section class="form-section">
                        <h2 class="section-title">Información Básica</h2>
                        <div class="form-group">
                            <label for="proyectName">Nombre del Proyecto *</label>
                            <input id="proyectName" v-model="formData.proyectName" type="text"
                                :class="['form-input', { 'invalid-input': isFieldInvalid('proyectName') }]" readonly
                                required />
                        </div>

                        <div class="form-group">
                            <label for="idProyect">ID del Proyecto *</label>
                            <input id="idProyect" v-model="formData.idProyect" type="text" class="form-input" readonly
                                required />
                        </div>

                        <div class="form-group">
                            <label for="elaborationDate">Fecha de Elaboración *</label>
                            <input id="elaborationDate" v-model="formData.elaborationDate" type="date"
                                class="form-input" readonly required />
                        </div>

                        <div class="form-group">
                            <label for="proyectPromotorTitle">Título del Promotor *</label>
                            <input id="proyectPromotorTitle" v-model="formData.proyectPromotorTitle" type="text"
                                class="form-input" required />
                        </div>
                        <div class="form-group">
                            <label for="proyectPromotor">Promotor del Proyecto *</label>
                            <input id="proyectPromotor" v-model="formData.proyectPromotor" type="text"
                                class="form-input" required />
                        </div>
                    </section>

                    <!-- Propósito y Justificación -->
                    <section class="form-section">
                        <h2 class="section-title">Propósito y Justificación</h2>
                        <div class="form-group">
                            <label for="purposeJustification">Propósito y Justificación del Proyecto *</label>
                            <textarea id="purposeJustification" v-model="formData.purposeJustificationProject"
                                class="form-input form-textarea" required></textarea>
                        </div>
                    </section>

                    <!-- Descripción del Alcance -->
                    <section class="form-section">
                        <h2 class="section-title">Descripción del Alcance</h2>
                        <div class="form-group">
                            <label for="scopeDescription">Descripción del Alcance *</label>
                            <textarea id="scopeDescription" v-model="formData.scopeDescription"
                                class="form-input form-textarea" required></textarea>
                        </div>
                    </section>

                    <!-- Requisitos de Alto Nivel -->
                    <section class="form-section">
                        <h2 class="section-title">Requisitos de Alto Nivel</h2>
                        <div v-for="(requirement, index) in formData.highLevelRequirements" :key="index"
                            class="stakeholder-row">
                            <div class="stakeholder-inputs">
                                <div class="form-group">
                                    <label :for="'requirementId' + index">Identificador de Requisito *</label>
                                    <input :id="'requirementId' + index" v-model="requirement.id" type="text"
                                        class="form-input" placeholder="Ingrese el identificador" required />
                                </div>
                                <div class="form-group">
                                    <label :for="'requirementDesc' + index">Descripción de Requisito *</label>
                                    <input :id="'requirementDesc' + index" v-model="requirement.description" type="text"
                                        class="form-input" placeholder="Ingrese la descripción" required />
                                </div>
                            </div>
                            <button v-if="formData.highLevelRequirements.length > 1" type="button" class="remove-button"
                                @click="removeRequirement(index)">
                                ×
                            </button>
                        </div>
                        <div class="add-button-container">
                            <button type="button" class="add-button" @click="addRequirement">
                                +
                            </button>
                        </div>
                    </section>

                    <!-- Estimación de Costos -->
                    <section class="form-section">
                        <h2 class="section-title">Estimación de Costos</h2>
                        <!-- Estimación de Costos -->
                        <div class="form-group">
                            <h4 class="subsection-subtitle">Estimación de Costos del Proyecto</h4>
                            <div v-for="(cost, index) in formData.costEstimation" :key="index" class="stakeholder-row">
                                <div class="stakeholder-inputs">
                                    <div class="form-group">
                                        <label :for="'expense' + index">Gastos *</label>
                                        <input :id="'expense' + index" v-model="cost.expense" type="text"
                                            class="form-input" placeholder="Nombre del gasto" required />
                                    </div>
                                    <div class="form-group">
                                        <label :for="'estimatedBudget' + index">Presupuesto Estimado *</label>
                                        <input :id="'estimatedBudget' + index" v-model="cost.estimatedBudget"
                                            type="number" class="form-input" placeholder="0" required />
                                    </div>
                                    <div class="form-group">
                                        <label :for="'spentToDate' + index">Gastado hasta la fecha *</label>
                                        <input :id="'spentToDate' + index" v-model="cost.spentToDate" type="number"
                                            class="form-input" placeholder="0" required />
                                    </div>
                                    <div class="form-group">
                                        <label :for="'estimateToComplete' + index">Estimación para completar *</label>
                                        <input :id="'estimateToComplete' + index" v-model="cost.estimateToComplete"
                                            type="number" class="form-input" placeholder="0" required />
                                    </div>
                                </div>
                                <button v-if="formData.costEstimation.length > 1" type="button" class="remove-button"
                                    @click="removeCost(index)">×</button>
                            </div>
                            <div class="add-button-container">
                                <button type="button" class="add-button" @click="addCost">+</button>
                            </div>
                        </div>
                    </section>




                    <!-- Sección de Análisis de costo-beneficio -->
                    <section class="form-section">
                        <h2 class="section-title">Análisis Costo-Beneficio</h2>
                        <!-- Análisis Costo-Beneficio -->
                        <div class="subsection">
                            <h3 class="subsection-title">Análisis Costo-Beneficio</h3>

                            <!-- Costos del Proyecto -->
                            <div class="form-group">
                                <h4 class="subsection-subtitle">Costos del Proyecto</h4>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="recurringCost">Costo Recurrente *</label>
                                        <input id="recurringCost"
                                            v-model="formData.costBenefitAnalysis.projectCosts.recurringCost"
                                            type="number" class="form-input" placeholder="0" required />
                                        <small class="input-help">Costos que se repiten regularmente durante la
                                            operación del proyecto</small>
                                    </div>
                                    <div class="form-group">
                                        <label for="nonRecurringCost">Costo No Recurrente *</label>
                                        <input id="nonRecurringCost"
                                            v-model="formData.costBenefitAnalysis.projectCosts.nonRecurringCost"
                                            type="number" class="form-input" placeholder="0" required />
                                        <small class="input-help">Costos únicos durante la implementación o inicio del
                                            proyecto</small>
                                    </div>
                                    <div class="form-group">
                                        <label for="capitalCosts">Costos de Capital *</label>
                                        <input id="capitalCosts"
                                            v-model="formData.costBenefitAnalysis.projectCosts.capitalCosts"
                                            type="number" class="form-input" placeholder="0" required />
                                        <small class="input-help">Gastos destinados a la adquisición o mejora de activos
                                            fijos</small>
                                    </div>
                                </div>
                            </div>

                            <!-- Beneficios -->
                            <div class="form-group">
                                <h4 class="subsection-subtitle">Beneficios (1 año)</h4>
                                <div class="stakeholder-inputs">
                                    <!-- Beneficio 1: Reducción de retrasos -->
                                    <div class="form-group">
                                        <label>Reducción de retrasos en un 30%</label>
                                        <input v-model="formData.costBenefitAnalysis.benefits.delays" 
                                            type="number" 
                                            class="form-input" 
                                            placeholder="0" 
                                            required />
                                        <small class="input-help">
                                            Representa el valor monetario obtenido por disminuir los retrasos en las operaciones
                                        </small>
                                    </div>
                                    <!-- Beneficio 2: Reducción de Sobrecostos -->
                                    <div class="form-group">
                                        <label>Reducción de Sobrecostos en un 30%</label>
                                        <input v-model="formData.costBenefitAnalysis.benefits.overruns" 
                                            type="number" 
                                            class="form-input" 
                                            placeholder="0" 
                                            required />
                                        <small class="input-help">
                                            Indica el ahorro o beneficio para controlar mejor los sobrecostos
                                        </small>
                                    </div>
                                    <!-- Beneficio 3: Mejora de Asignación de Recursos -->
                                    <div class="form-group">
                                        <label>Mejora de la Asignación de Recursos</label>
                                        <input v-model="formData.costBenefitAnalysis.benefits.resources" 
                                            type="number" 
                                            class="form-input" 
                                            placeholder="0" 
                                            required />
                                        <small class="input-help">
                                            Refleja el beneficio obtenido al utilizar los recursos de manera más eficiente
                                        </small>
                                    </div>
                                </div>
                            </div>

                            <!-- Totales -->
                            <div class="totals-section">
                                <div class="form-row">
                                    <div class="form-group">
                                        <label>Beneficios Totales del Proyecto</label>
                                        <input :value="calculateTotalBenefits()" type="number" class="form-input"
                                            readonly />
                                    </div>
                                    <div class="form-group">
                                        <label>Beneficio Neto del Proyecto</label>
                                        <input :value="calculateNetBenefit()" type="number" class="form-input"
                                            readonly />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </section>

                    <!-- Campos adicionales -->
                    <section class="form-section">
                        <h2 class="section-title">Información Adicional</h2>
                        <div class="form-group">
                            <label for="boundaries">Límites *</label>
                            <textarea id="boundaries" v-model="formData.boundaries" class="form-input form-textarea"
                                required></textarea>
                        </div>

                        <div class="form-group">
                            <label for="strategy">Estrategia *</label>
                            <textarea id="strategy" v-model="formData.strategy" class="form-input form-textarea"
                                required></textarea>
                        </div>

                        <div class="form-group">
                            <label for="deliverables">Entregables *</label>
                            <textarea id="deliverables" v-model="formData.deliverables" class="form-input form-textarea"
                                required></textarea>
                        </div>

                        <div class="form-group">
                            <label for="acceptanceCriteria">Criterios de Aceptación *</label>
                            <textarea id="acceptanceCriteria" v-model="formData.acceptanceCriteria"
                                class="form-input form-textarea" required></textarea>
                        </div>

                        <div class="form-group">
                            <label for="restrictions">Restricciones *</label>
                            <textarea id="restrictions" v-model="formData.restrictions" class="form-input form-textarea"
                                required></textarea>
                        </div>
                    </section>

                    <!-- Botones de acción -->
                    <section class="form-actions">
                        <button type="button" class="cancel-button" @click="showCancelConfirmation = true">
                            Cancelar
                        </button>
                        <button type="button" class="save-button" :disabled="loading"
                            @click="showSaveConfirmation = true">
                            {{ loading ? 'Guardando...' : 'Guardar' }}
                        </button>
                        <button type="submit" class="submit-button" :disabled="loading">
                            {{ loading ? 'Creando...' : 'Crear Documento' }}
                        </button>
                    </section>
                </form>

                <!-- Diálogos de confirmación -->
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

                <AlertPopup :show="alert.show" :title="alert.title" :message="alert.message" :type="alert.type"
                    @confirm="handleAlertConfirm" />
            </div>
        </main>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useAuthStore } from '../stores/auth';
import { useProjectStore } from '../stores/project';
import AlertPopup from '../components/AlertPopup.vue';
import { useRouter } from 'vue-router';

const loading = ref(false);
const errorMessage = ref('');
const AuthStore = useAuthStore();
const ProjectStore = useProjectStore();
const showCancelConfirmation = ref(false);
const showSaveConfirmation = ref(false);
const router = useRouter();

const formData = reactive({
    proyectName: '',
    idProyect: '',
    elaborationDate: new Date().toISOString().split('T')[0],
    purposeJustificationProject: '',
    scopeDescription: '',
    highLevelRequirements: [{
        id: '',
        description: ''
    }],
    boundaries: '',
    strategy: '',
    deliverables: '',
    acceptanceCriteria: '',
    restrictions: '',
    costEstimation: [{  // Cambiado de objeto a array con un solo elemento inicial
        expense: '',
        estimatedBudget: 0,
        spentToDate: 0,
        estimateToComplete: 0
    }],
    costBenefitAnalysis: {
        projectCosts: {
            recurringCost: 0,
            nonRecurringCost: 0,
            capitalCosts: 0
        },
        benefits: {
            delays: 0,
            overruns: 0,
            resources: 0
        },
        totalBenefits: 0,
        netBenefit: 0
    },
    proyectPromotorTitle: '',
    proyectPromotor: '',
    deliverables: ''
});

const alert = reactive({
    show: false,
    title: '',
    message: '',
    type: 'info',
});

// Funciones principales
const addRequirement = () => {
    formData.highLevelRequirements.push({
        id: '',
        description: ''
    });
};

const removeRequirement = (index) => {
    formData.highLevelRequirements.splice(index, 1);
};

// Funciones de cálculo
const calculateTotalBenefits = () => {
    const benefits = formData.costBenefitAnalysis.benefits;
    return Number(benefits.delays) + Number(benefits.overruns) + Number(benefits.resources);
};

const calculateTotalCosts = () => {
    const costs = formData.costBenefitAnalysis.projectCosts;
    return Number(costs.recurringCost) + Number(costs.nonRecurringCost) + Number(costs.capitalCosts);
};

const calculateNetBenefit = () => {
    return calculateTotalBenefits() - calculateTotalCosts();
};

// Funciones de validación
const validateForm = () => {
    return true; // Implementar validación específica si es necesaria
};

const isFieldInvalid = (fieldName) => {
    return false; // Implementar validación específica si es necesaria
};

// Funciones de manejo de eventos
const handleCancel = () => {
    showCancelConfirmation.value = false;
    router.push('/ProjectManagementAreas');
};

const handleSubmit = async () => {
    if (!validateForm()) {
        errorMessage.value = 'Por favor, complete todos los campos obligatorios';
        return;
    }

    try {
        loading.value = true;
        errorMessage.value = '';

        const userId = AuthStore.userId;
        const token = AuthStore.token;
        const projectId = ProjectStore.projectId;

        // Formatear los datos según la convención
        const formattedData = {
            ...formData,
            costBenefitAnalysis: formatCostBenefitTable(formData),
            highLevelRequirements: formatHighLevelRequirements(formData),
            costEstimation: formatCostEstimation(formData),
        };

        console.log("JSON para Postman (handleSubmit):", JSON.stringify(formattedData, null, 2));

        const response = await fetch(`http://localhost:8080/api/documents/project-scope-statement/generate?idExit=${projectId}`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formattedData),
            
        });

        if (!response.ok) {
            throw new Error('Error al crear el documento');
        }

        const blob = await response.blob();
        const url = URL.createObjectURL(blob);
        window.open(url);

    } catch (error) {
        showAlert('Error', error.message, 'error');
    } finally {
        loading.value = false;
    }
};

const formatCostBenefitTable = (data) => {
    const costBenefit = {
        with: {
            costs: {
                recurring: data.costBenefitAnalysis.projectCosts.recurringCost || 0,
                nonRecurring: data.costBenefitAnalysis.projectCosts.nonRecurring || 0,
                capital: data.costBenefitAnalysis.projectCosts.capitalCosts || 0
            },
            benefits: {
                delays: data.costBenefitAnalysis.benefits.delays || 0,
                overruns: data.costBenefitAnalysis.benefits.overruns || 0,
                resources: data.costBenefitAnalysis.benefits.resources || 0
            }
        },
        without: {
            benefits: {
                delays: -15000,
                overruns: -170000,
                resources: -22000
            }
        }
    };

    const withTotal = {
        costs: costBenefit.with.costs.recurring + costBenefit.with.costs.nonRecurring + costBenefit.with.costs.capital,
        benefits: costBenefit.with.benefits.delays + costBenefit.with.benefits.overruns + costBenefit.with.benefits.resources
    };

    const withoutTotal = {
        costs: 0,
        benefits: costBenefit.without.benefits.delays + costBenefit.without.benefits.overruns + costBenefit.without.benefits.resources
    };

    let table = "";
    table += "& ,Con PMD Project,Sin PMD Project&\n";
    table += "Costs of Project, , &\n";
    table += `Recurring Cost,${costBenefit.with.costs.recurring},0&\n`;
    table += `Non-Recurring Cost,${costBenefit.with.costs.nonRecurring},0&\n`;
    table += `Capital Costs,${costBenefit.with.costs.capital},0&\n`;
    table += `Total Cost of PMD Project,${withTotal.costs},0&\n`;
    table += " , , &\n";
    table += "Benefits (1 year), , &\n";
    table += `Reduce Delays by 30%,${costBenefit.with.benefits.delays},${costBenefit.without.benefits.delays}&\n`;
    table += `Reduce Overruns by 30%,${costBenefit.with.benefits.overruns},${costBenefit.without.benefits.overruns}&\n`;
    table += `Improve Resource Allocation,${costBenefit.with.benefits.resources},${costBenefit.without.benefits.resources}&\n`;
    table += `Total Benefits of PMD Project,${withTotal.benefits},${withoutTotal.benefits}&\n`;
    table += " , , &\n";
    table += `Net Benefits of PMD Project,${withTotal.benefits - withTotal.costs},${withoutTotal.benefits}&`;

    return table;
};

const formatHighLevelRequirements = (data) => {
    let table = "&Identificador de Requisito,Descripción de Requisito&";
    data.highLevelRequirements.forEach(req => {
        table += `${req.id},${req.description}&`;
    });
    return table;
};

// Formato para Estimación de Costos 
const formatCostEstimation = (data) => {
    let table = "&Gastos,Presupuesto Estimado,Gastado hasta la fecha,Estimación para completar,Diferencia&";
    data.costEstimation.forEach(cost => {
        const difference = Number(cost.estimatedBudget) - (Number(cost.spentToDate) + Number(cost.estimateToComplete));
        table += `${cost.expense},${cost.estimatedBudget},${cost.spentToDate},${cost.estimateToComplete},${difference}&`;
    });
    return table;
};


const confirmSave = async () => {
    try {
        loading.value = true;
        showSaveConfirmation.value = false;
        await handleSave();
        showAlert('Éxito', 'Los datos se han guardado correctamente', 'success');
    } catch (error) {
        showAlert('Error', 'Error al guardar los datos', 'error');
    } finally {
        loading.value = false;
    }
};

const handleSave = async () => {
    const userId = AuthStore.userId;
    const token = AuthStore.token;
    const projectId = ProjectStore.projectId;

    const dataToSend = {
        ...formData,
        highLevelRequirements: formatHighLevelRequirements(formData),
        costEstimation: formatCostEstimation(formData),
        costBenefitAnalysis: formatCostBenefitTable(formData)
    };

    console.log("JSON para Postman:", JSON.stringify(dataToSend, null, 2));

    // return await response.json();
};

// Funciones de alertas
const showAlert = (title, message, type = 'info') => {
    alert.title = title;
    alert.message = message;
    alert.type = type;
    alert.show = true;
};

const addCost = () => {
    formData.costEstimation.push({
        expense: '',
        estimatedBudget: 0,
        spentToDate: 0,
        estimateToComplete: 0
    });
};

const removeCost = (index) => {
    if (formData.costEstimation.length > 1) {
        formData.costEstimation.splice(index, 1);
    }
};

const handleAlertConfirm = () => {
    alert.show = false;
    if (alert.type === 'error' && alert.message.includes('Sesión no iniciada')) {
        navigateTo('/login');
    }
};

// Inicialización
onMounted(async () => {
    try {
        loading.value = true;
        const projectId = ProjectStore.projectId;
        const token = AuthStore.token;

        const response = await fetch(`http://localhost:8080/api/v1/proyect/getById/${projectId}`, {
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        });

        if (response.ok) {
            const data = await response.json();
            formData.proyectName = data.nameProyect;
            formData.idProyect = data.idProyecto;
            formData.elaborationDate = data.startDate;
        }
    } catch (error) {
        showAlert('Error', 'Error al cargar los datos del proyecto', 'error');
    } finally {
        loading.value = false;
    }
});
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

.scope-form {
    display: flex;
    flex-direction: column;
    gap: 2rem;
}

.form-section {
    border-bottom: 1px solid #eee;
    padding-bottom: 2rem;
    margin-bottom: 2rem;
}

.section-title {
    font-size: 1.25rem;
    color: #000000;
    margin-bottom: 1.5rem;
}

.subsection-title {
    font-size: 1.1rem;
    color: #333;
    margin-bottom: 1rem;
}

.form-group {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
    margin-bottom: 1.5rem;
    color: #000000;
}

.form-row {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 1.5rem;
}

label {
    font-weight: 500;
    color: #333;
}

.form-row {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 1rem;
    margin-bottom: 1rem;
}

.stakeholder-row {
    display: flex;
    gap: 1rem;
    align-items: start;
    margin-bottom: 1rem;
    background-color: #f9f9f9;
    padding: 1rem;
    border-radius: 4px;
    position: relative;
}

.stakeholder-inputs {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 1rem;
    flex: 1;
}

.add-button-container {
    display: flex;
    justify-content: center;
    margin-top: 1rem;
}

.add-button {
    width: 30px;
    height: 30px;
    border-radius: 50%;
    background-color: #00B8B0;
    color: white;
    border: none;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 20px;
}

.remove-button {
    width: 24px;
    height: 24px;
    border-radius: 50%;
    background-color: #dc3545;
    color: white;
    border: none;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 16px;
    position: absolute;
    top: 1rem;
    right: 1rem;
}

.subsection-title {
    font-size: 1rem;
    color: #333;
    margin-bottom: 1rem;
    font-weight: 500;
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

.form-input:focus {
    outline: none;
    border-color: #00B8B0;
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
}

.submit-button,
.cancel-button,
.save-button {
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

.save-button {
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

.cancel-button {
    background-color: #f5f5f5;
    color: #666;
    border: 1px solid #ddd;
}

.cancel-button:hover {
    background-color: #eee;
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
    color: #333;
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

.stakeholder-row {
    position: relative;
    margin-bottom: 1.5rem;
    background-color: #ffffff;
    padding: 1rem;
    border-radius: 4px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.stakeholder-inputs {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 1rem;
}

.add-stakeholder-container {
    display: flex;
    justify-content: center;
    margin-top: 1.5rem;
}

.add-stakeholder-button {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 40px;
    height: 40px;
    border-radius: 50%;
    background-color: #00B8B0;
    color: white;
    border: none;
    cursor: pointer;
    font-size: 1.5rem;
    transition: background-color 0.3s;
}

.add-stakeholder-button:hover {
    background-color: #009B94;
}

.remove-stakeholder-button {
    position: absolute;
    right: 1rem;
    top: 1rem;
    width: 24px;
    height: 24px;
    border-radius: 50%;
    background-color: #dc3545;
    color: white;
    border: none;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 1rem;
    transition: background-color 0.3s;
}

.remove-stakeholder-button:hover {
    background-color: #c82333;
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

.invalid-feedback {
    color: #dc3545;
    font-size: 0.875rem;
    margin-top: 0.25rem;
    display: block;
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

.pdf-actions {
    display: flex;
    gap: 1rem;
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
    text-decoration: none;
}

.input-help {
    font-size: 0.875rem;
    color: #666;
}

.download-button {
    background-color: #00B8B0;
    color: white;
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

@media (max-width: 768px) {
    .stakeholder-inputs {
        grid-template-columns: 1fr;
    }

    .form-container {
        padding: 1rem;
    }

    .form-actions {
        flex-direction: column;
    }

    .submit-button,
    .cancel-button,
    .save-button {
        width: 100%;
    }

    .modal-content {
        width: 95%;
        padding: 1rem;
    }

    .pdf-header {
        flex-direction: column;
        align-items: start;
        gap: 1rem;
    }

    .pdf-actions {
        width: 100%;
    }

    .pdf-button {
        width: 100%;
        justify-content: center;
    }
}
</style>
