<template>
    <div class="container">
 
      <section class="form-section">
        <div class="form-container">
          <h1 class="form-title">Registrarse</h1>
          
          <!-- Success Snackbar --> 
          <v-snackbar v-model="successSnackbar" :timeout="2000" class="top-snackbar" color="success"> 
            Registro exitoso. Redirigiendo a inicio de sesion... 
            <v-progress-linear v-model="progress" color="dark green" height="4"></v-progress-linear>
          </v-snackbar>

          <!-- ^[A-Za-zÀ-ÖØ-öø-ÿ]+([ '-][A-Za-zÀ-ÖØ-öø-ÿ]+)*$ Validated any number of names with valid special characters in them--> 
          <form @submit.prevent="handleSubmit" class="form">
            <input
              v-model="formData.names"
              type="text"
              placeholder="Nombres"
              class="input"
              pattern="[A-Za-zÀ-ÖØ-öø-ÿ]+([ '-][A-Za-zÀ-ÖØ-öø-ÿ]+)*"
              maxlength="1000"
              required="true"
            />

            <input
              v-model="formData.secondNames"
              type="text"
              placeholder="Apellidos"
              class="input"
              pattern="[A-Za-zÀ-ÖØ-öø-ÿ]+([ '-][A-Za-zÀ-ÖØ-öø-ÿ]+)*"
              maxlength="1000"
              required="true"
            />
  
            <input
              v-model="formData.email"
              type="email"
              placeholder="Correo electrónico (ej: name@example.cl)"
              class="input"
              maxlength="1000"
              required="true"
            />
  
            <input
              v-model="formData.password"
              type="password"
              placeholder="Contraseña"
              class="input"
              required="true"
              maxlength="1000"
              minlength="8"
              @input="triggerConfirmPasswordValidation"
            />

            <input
              v-model="formData.confirm_password"
              type="password"
              placeholder="Confirmar Contraseña"
              :class="['input', { invalid: !passwordsMatch }]"
              required="true"
              maxlength="1000"
              @input="validatePasswords($event)"
              ref="confirmPasswordInput"
            />
  
            <input
              v-model="formData.phoneNumber"
              type="tel"
              placeholder="Número de Telefono (ej: +56 9 1234 5678)"
              class="input"
              pattern="^((\+|00)[1-9]{2}[1-9]?)( ?[1-9][1-9]?)( ?[0-9]){8}$"
              required="true"
            />
  
            <button type="submit" class="submit-button">
              Registrarse
            </button>
          </form>

          <p v-if="errorMessage" class="error-message">
            {{ errorMessage }}
          </p>
  
          <div class="login-prompt">
            <span>¿Ya tienes una cuenta en esta aplicación?</span>
            <a href="/login" class="login-link"> Inicia sesión aquí</a>
          </div>
        </div>
      </section>
    </div>
  </template>
  
  <script setup>
  import { reactive, ref } from 'vue'
  
  const formData = reactive({
    names: '',
    secondNames: '',
    email: '',
    password: '',
    confirm_password: '',
    phoneNumber: ''
  })

  const passwordsMatch = ref(true);
  const confirmPasswordInput = ref(null);
  const successSnackbar = ref(false);
  const progress = ref(100);

  const errorMessage = ref('');

  const url = "http://localhost:8080/api/v1/auth";

  // Validating name (both first and second names)
  function isNameValid(name) {
    const nameRegex = /^[A-Za-zÀ-ÖØ-öø-ÿ]+([ '-][A-Za-zÀ-ÖØ-öø-ÿ]+)*$/;
    if (!nameRegex.test(name)) {
      errorMessage.value = 'El nombre o apellido no es válido.';
      return false;
    }
    return true;
  }
  
  // Validating phone number using regex
  function isPhoneNumberValid(phoneNumber) {
    const phoneRegex = /^((\+|00)[1-9]{2}[1-9]?)( ?[1-9][1-9]?)( ?[0-9]){8}$/;
    if (!phoneRegex.test(phoneNumber)) {
      errorMessage.value = 'Número de teléfono no válido.';
      return false;
    } 
    return true;
  }

  const checkPasswordsMatch = () => {
    passwordsMatch.value = formData.password === formData.confirm_password;
    return passwordsMatch.value
  }

  const validatePasswords = (event) => {
    const confirmPasswordInput = event.target;

    checkPasswordsMatch()

    if (!passwordsMatch.value) {
      confirmPasswordInput.setCustomValidity('Las contraseñas no coinciden');
    } else {
      confirmPasswordInput.setCustomValidity(''); // Reset message if valid
    }
  };

  const triggerConfirmPasswordValidation = () => {
    // Trigger the input event programmatically
    const confirmPasswordField = confirmPasswordInput.value;
    if (confirmPasswordField) {
      confirmPasswordField.dispatchEvent(new Event("input"));
    }
  };

  const triggerValidationMessage = () => {
    // Trigger the input report event programmatically
    const confirmPasswordField = confirmPasswordInput.value;
    if (confirmPasswordField) {
      confirmPasswordField.reportValidity(); // Show the validation message
    }
  };

  const triggerSnackBar = () => {
    successSnackbar.value = true;
    progress.value = 100; // Start the progress bar countdown 
    const interval = setInterval(() => { 
      if (progress.value > 0) { 
        progress.value -= 100 / 20; // Decrement by 100 / 20 every 100 ms to match 2 seconds 
      } else { 
        clearInterval(interval); 
      } 
    }, 90); // Finish slightly early for better effect
  } 
  
  const handleSubmit = async () => {
    if (! checkPasswordsMatch()) {
      triggerConfirmPasswordValidation(); // Ensure passwords are validated before submission
      triggerValidationMessage();
      return
    }
    if (! isNameValid(formData.names) || ! isNameValid(formData.secondNames)){ // Ensure names are valid
      return
    }
    if (! isPhoneNumberValid(formData.phoneNumber)) { // Ensure phone is valid
      return
    }
    try {
        try {
          errorMessage.value = '';
          const response = await $fetch(url + '/register', {
            method: 'POST',
            body: { 
              names: formData.names,
              secondNames: formData.secondNames,
              email: formData.email,
              password: formData.password,
              phoneNumber: formData.phoneNumber
            }
          })
          console.log('Registered successfully');

          // Succesful login snackbar and progress bar
          triggerSnackBar()

          // Redirect to login on successful register
          await new Promise(resolve => setTimeout(resolve, 2000));
          await navigateTo('/login')

        } catch (error) {
          if (error.response && error.response.status) { 
            switch (error.response.status) {
              case 400: // Bad Request
                errorMessage.value = 'Por favor, verifica los datos ingresados.';
                break;
              case 401: // Unauthorized
                errorMessage.value = 'Usuario con correo indicado ya existe.';
                break;
              case 423: // Locked
                errorMessage.value = 'La creacion de cuentas se encuentra desactivada actualmente.';
                break;
              default:
                errorMessage.value = 'Error en Registro de Usuario. Por favor, intenta más tarde.';
                break;
            }
          } 
          else {
            errorMessage.value = 'Error de conexion, pruebe más tarde'
            console.error('Connection error:', error); 
          }
        }
      } catch (error) {
        errorMessage.value = 'Ha ocurrido un error, intente más tarde'
        console.error('Registration error:', error)
      }
  }

  </script>
  
<style scoped>

.top-snackbar { 
  top: 70px !important; 
  bottom: auto !important; 
}

.container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100%;
    background-color: #ffffff;
    overflow: auto;
}

.form-section {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
}

.form-container {
    background-color: #d9d9d9;
    border-radius: 10px;
    width: 100%;
    max-width: 600px;
    padding: 2rem;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    text-align: center;
    overflow: auto;
}

.form-title {
    font-size: 1.5rem;
    font-weight: bold;
    color: #000000;
    margin-bottom: 1rem;
}

.form {
    display: flex;
    flex-direction: column;
    gap: 1rem;
}

.input {
    width: 100%;
    padding: 0.75rem;
    border: 1px solid #ccc;
    border-radius: 5px;
    background-color: #fff;
    font-size: 1rem;
    color: rgb(0, 0, 0);
}

.input:focus {
    outline: none;
    border-color: #00B8B0;
    box-shadow: 0 0 0 2px rgba(0, 184, 176, 0.3);
}

.input::placeholder {
    color: #bdbdbd;
}

.input.invalid {
  border-color: red;
  background-color: #ffe6e6;
}

.submit-button {
    background-color: #00B8B0;
    color: #fff;
    border: none;
    padding: 0.75rem;
    border-radius: 5px;
    font-size: 1rem;
    cursor: pointer;
    transition: background-color 0.3s;
}

.submit-button:hover {
    background-color: #009B94;
}

.login-prompt {
    margin-top: 1rem;
    font-size: 0.875rem;
    color: #666;
}

.login-link {
    color: #00B8B0;
    text-decoration: none;
    font-weight: bold;
}

.login-link:hover {
    text-decoration: underline;
}

.error-message {
  color: #dc3545;
  text-align: center;
  margin-top: 10px;
  padding: 10px;
  border-radius: 5px;
  background-color: #ffe6e6;
}
</style>
