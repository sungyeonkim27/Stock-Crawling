document.addEventListener('DOMContentLoaded', function() {

    // 검증 통과 여부 필드
    let isUsernameValid = false;
    let isPasswordValid = false;
    let isPasswordMatchValid = false;

    const usernameInput = document.getElementById("username");
    const passwordInput = document.getElementById("password");
    const passwordConfirmInput = document.getElementById("passwordConfirm");

    const signForm = document.querySelector("form");

    // 버튼 활성화/비활성화
    updateSubmitButton();


    let debounceTimer;

    //아이디 검증
    usernameInput.addEventListener('input', function(event) {
        const InputValue = event.target.value;

        if (InputValue.length === 0) {
            clearMessage('username-message');
            isUsernameValid = false;
            updateSubmitButton();
            return;
        }

        if (InputValue.length < 6 || InputValue.length > 30) {
            showError('username-message', "아이디의 길이는 6자 이상 30자이하로 입력해야 합니다.");
            isUsernameValid = false;
            updateSubmitButton();
            return;
        }

        clearTimeout(debounceTimer);

        debounceTimer = setTimeout(() => {
            // API 호출
            fetch('/api/check-username?username=' + encodeURIComponent(InputValue))
                .then(response => response.json())
                .then(data => {
                if (data.exists) {
                    showError('username-message', "이미 존재하는 아이디 입니다.")
                    isUsernameValid = false;
                    updateSubmitButton();
                } else {
                    showSuccess('username-message', "사용가능한 아이디 입니다.")
                    isUsernameValid = true;
                    updateSubmitButton();
                }
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        }, 300);


    })

    // 비밀번호 유효성 검증
    passwordInput.addEventListener('input', function(event) {
        const password = event.target.value;

        if (password.length === 0) {
            clearMessage('password-message');
            isPasswordValid = false;
            updateSubmitButton();
            return;
        }

        // 비밀번호 검증
        if (password.length < 8 || password.length > 20) {
            showError('password-message',"비밀번호는 8자 이상 20자 이하여야합니다.");
            isPasswordValid = false;
            updateSubmitButton();
        } else if (!/\d/.test(password)) {
            showError('password-message', "비밀번호에는 숫자가 하나이상 포함되어야 합니다.");
            isPasswordValid = false;
            updateSubmitButton();
        } else {
            showSuccess('password-message', "사용 가능한 비밀번호입니다.");
            isPasswordValid = true;
            updateSubmitButton();
        }

    })

    // 비밀번호 확인 검증 로직
    passwordInput.addEventListener('input', checkPasswordMatch)
    passwordConfirmInput.addEventListener('input', checkPasswordMatch)

    // form 제출 전 최종 검증 기능
    signForm.addEventListener('submit', function(event) {
        if (!isUsernameValid || !isPasswordValid || !isPasswordMatchValid) {

            event.preventDefault();

            alert("모든 항목을 올바르게 입력하세요");

            if (!isUsernameValid) {
                showError('username-message', "아이디를 확인하세요");
            }
            if (!isPasswordValid) {
                showError('password-message', "비밀번호를 확인하세요");
            }
            if (!isPasswordMatchValid) {
                showError('passwordConfirm-message', "비밀번호 확인을 확인하세요");
            }

            return;
        }

    })


    // 에러메세지 보여주기
    function showError(elementById, message) {
        const messageElement = document.getElementById(elementById);
        messageElement.textContent = message;
        messageElement.classList.remove('success');
        messageElement.classList.add('error');
    }

    // 성공메세지 보여주기
    function showSuccess(elementById, message) {
        const messageElement = document.getElementById(elementById);
        messageElement.textContent = message;
        messageElement.classList.remove('error');
        messageElement.classList.add('success');
    }

    // 비밀번호 확인
    function checkPasswordMatch() {
        const password = passwordInput.value;
        const passwordConfirm = passwordConfirmInput.value;
        if (passwordConfirm === '') {
            // 아직 입력 없음
            isPasswordMatchValid = false;
            updateSubmitButton();
        } else if (password === passwordConfirm) {
            showSuccess('passwordConfirm-message', "비밀번호가 일치합니다.");
            isPasswordMatchValid = true;
            updateSubmitButton();
        } else {
            showError('passwordConfirm-message', "비밀번호가 일치하지 않습니다.");
            isPasswordMatchValid = false;
            updateSubmitButton();
        }
    }

    // 제출 버튼 비활성화 기능
    function updateSubmitButton() {
        const submitButton = document.querySelector('button[type="submit"]');

        if (isUsernameValid && isPasswordValid && isPasswordMatchValid) {

            submitButton.disabled = false;
            submitButton.style.opacity = '1'; // 완전히 보이게
            submitButton.style.cursor = 'pointer'; // 클릭 가능 표시
        } else {
            submitButton.disabled = true;
            submitButton.style.opacity = '0.5'; // 완전히 보이게
            submitButton.style.cursor = 'not-allowed'; // 클릭 금지 표시
        }
    }

    // 메세지 비우기
    function clearMessage(elementId) {
        const messageElement = document.getElementById(elementId);
        messageElement.textContent = '';
        messageElement.classList.remove('error', 'success');
    }

});