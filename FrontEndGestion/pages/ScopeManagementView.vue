<template>
    <div class="page-container">
        <header class="header">
            <div class="logo">
                <img src="../assets/usach-logo.png" alt="USACH" class="usach-logo" />
            </div>
        </header>

        <main class="main-content">
            <div class="management-container">
                <h1 class="section-title">Gestión del alcance</h1>
                <p class="section-subtitle">Selecciona que área quieres abordar.</p>

                <div class="management-areas">
                    <button v-for="area in managementAreas" :key="area.id" class="area-button"
                        @click="handleAreaClick(area)">
                        <span class="area-number">{{ area.id }}.</span>
                        <span class="area-title">{{ area.title }}</span>
                    </button>
                </div>
            </div>
        </main>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/auth';
import { useProjectStore } from '../stores/project';
import { onMounted } from 'vue';
import { useManagementsStore } from '../stores/Managements';

const router = useRouter();
const AuthStore = useAuthStore();
const ProjectStore = useProjectStore();
const ManagementsStore = useManagementsStore();

const managementAreas = [
    {
        id: 1,
        title: 'Planificar la Gestión del Alcance (Planeación)',
        route: '/scope-planning'
    },
    {
        id: 2,
        title: 'Recopilar los Requisitos (Planeación)',
        route: '/requirements'
    },
    {
        id: 3,
        title: 'Definir el Alcance (Planeación)',
        route: '/scope-definition'
    },
    {
        id: 4,
        title: 'Crear la Estructura Desglosada del Trabajo (WBS) (Planeación)',
        route: '/wbs'
    },
    {
        id: 5,
        title: 'Validar el Alcance (Monitoreo)',
        route: '/scope-validation'
    },
    {
        id: 6,
        title: 'Controlar el Alcance (Monitoreo)',
        route: '/scope-control'
    }
];

const handleAreaClick = (area) => {
    router.push(area.route);
};

onMounted(async () => {
    try {
        // Verificar si hay token y projectId
        if (!AuthStore.token || !ProjectStore.projectId) {
            console.error('No hay token o projectId');
            router.push('/login');
            return;
        }

        // Codificar los parámetros de la URL
        const nameManagement = encodeURIComponent("Gestión del Alcance");
        const idProyect = encodeURIComponent(ProjectStore.projectId);

        // Primera petición para verificar si existe el management
        const checkResponse = await fetch(
            `http://localhost:8080/api/v1/management/getByNameAndIdProyect?nameManagement=${nameManagement}&idProyect=${idProyect}`,
            {
                headers: {
                    'Authorization': `Bearer ${AuthStore.token}`,
                    'Content-Type': 'application/json',
                },
            }
        );

        if (checkResponse.status === 200) {
            const managementData = await checkResponse.json();
            ManagementsStore.managementId = managementData.idManagement;  
        } else if (checkResponse.status === 404) {
            // Si no existe, creamos uno nuevo
            const createResponse = await fetch(
                'http://localhost:8080/api/v1/management/create',
                {
                    method: 'POST',
                    headers: {
                        'Authorization': `Bearer ${AuthStore.token}`,
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        idProyecto: ProjectStore.projectId,
                        nameManagement: "Gestión del Alcance",
                        description: "Gestión del Alcance del Proyecto"
                    })
                }
            );

            if (createResponse.ok) {
                const newManagementData = await createResponse.json();
                ManagementsStore.managementId = newManagementData.idManagement;  
            } else {
                console.error('Error al crear el management');
            }
        }
    } catch (error) {
        console.error('Error en la gestión del management:', error);
    }
});
</script>

<style scoped>
.page-container {
    min-height: 100vh;
    background-color: #f5f5f5;
}

.header {
    background-color: #00B8B0;
    padding: 1rem 2rem;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.usach-logo {
    height: 40px;
}

.main-content {
    padding: 2rem;
    max-width: 1200px;
    margin: 0 auto;
}

.management-container {
    background: white;
    border-radius: 8px;
    padding: 2rem;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.section-title {
    font-size: 1.5rem;
    color: #333;
    margin-bottom: 0.5rem;
    font-weight: bold;
}

.section-subtitle {
    color: #666;
    margin-bottom: 2rem;
}

.management-areas {
    display: flex;
    flex-direction: column;
    gap: 1rem;
}

.area-button {
    display: flex;
    align-items: center;
    width: 100%;
    padding: 1rem;
    background-color: #00B8B0;
    color: white;
    border: none;
    border-radius: 25px;
    text-align: left;
    cursor: pointer;
    transition: background-color 0.2s;
    font-size: 1rem;
}

.area-button:hover {
    background-color: #009B94;
}

.area-number {
    margin-right: 1rem;
}

.area-title {
    flex: 1;
}

@media (max-width: 768px) {
    .main-content {
        padding: 1rem;
    }

    .management-container {
        padding: 1.5rem;
    }

    .area-button {
        padding: 0.75rem;
        font-size: 0.9rem;
    }
}
</style>