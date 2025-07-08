document.addEventListener('DOMContentLoaded', function() {
    
    // ✅ Garantir que o form existe antes de adicionar event listener
    const loginForm = document.getElementById('loginForm');
    if (!loginForm) return;
    
    loginForm.addEventListener('submit', function(e) {
        let isValid = true;
        
        // ✅ Validar Email
        const email = document.getElementById('email');
        const emailError = document.getElementById('emailError');
        const emailValue = email.value.trim();
        
        // Limpar erros anteriores
        emailError.classList.add('hidden');
        email.classList.remove('border-red-500');
        
        if (emailValue === '') {
            emailError.textContent = 'O e-mail é obrigatório';
            emailError.classList.remove('hidden');
            email.classList.add('border-red-500');
            isValid = false;
        } else if (!isValidEmail(emailValue)) {
            emailError.textContent = 'E-mail inválido';
            emailError.classList.remove('hidden');
            email.classList.add('border-red-500');
            isValid = false;
        }
        
        // ✅ Validar Senha
        const senha = document.getElementById('senha');
        const senhaError = document.getElementById('senhaError');
        const senhaValue = senha.value.trim();
        
        // Limpar erros anteriores
        senhaError.classList.add('hidden');
        senha.classList.remove('border-red-500');
        
        if (senhaValue === '') {
            senhaError.textContent = 'A senha é obrigatória';
            senhaError.classList.remove('hidden');
            senha.classList.add('border-red-500');
            isValid = false;
        }
        
        // ✅ Se tem erros, impede o envio
        if (!isValid) {
            e.preventDefault();
            return false;
        }
        
        // ✅ Se não tem erros, permite o envio para Spring Security
        return true;
    });

    // ✅ Função para validar formato de email
    function isValidEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }

    // ✅ Limpar erros quando user digita
    const emailInput = document.getElementById('email');
    const senhaInput = document.getElementById('senha');
    
    if (emailInput) {
        emailInput.addEventListener('input', function() {
            const emailError = document.getElementById('emailError');
            emailError.classList.add('hidden');
            this.classList.remove('border-red-500');
        });
    }

    if (senhaInput) {
        senhaInput.addEventListener('input', function() {
            const senhaError = document.getElementById('senhaError');
            senhaError.classList.add('hidden');
            this.classList.remove('border-red-500');
        });
    }
});