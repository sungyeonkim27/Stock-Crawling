document.addEventListener('DOMContentLoaded', function() {

    // 검증 통과 여부 필드
    let isCurrentPasswordValid = false;
    let isNewPasswordValid = false;
    let isNewPasswordMatchValid = false;


    const currentPasswordInput = document.getElementById("currentPassword");
    const newPasswordInput = document.getElementById("newPassword");
    const newPasswordConfirmInput = document.getElementById("newPasswordConfirm");

    const signForm = document.querySelector("form");

    // 버튼 활성화/비활성화
    updateSubmitButton();

    // 현재 비밀번호 입력란 확인
    currentPasswordInput.addEventListener('input', function(event) {
        const InputValue = event.target.value;

        if (InputValue.length === 0) {
            clearMessage('currentPassword-message');
            isCurrentPasswordValid = false;
            updateSubmitButton();
            return;
        } else {
            isCurrentPasswordValid = true;
            updateSubmitButton();
        }

        // 현재 비밀번호 변경 시 새 비밀번호 재검증
        validateNewPassword();
    })

    // 새 비밀번호 유효성 검증
    newPasswordInput.addEventListener('input', function(event) {
        validateNewPassword();
    })

    // 새 비밀번호 검증 함수
    function validateNewPassword() {
        const currentPassword = currentPasswordInput.value;
        const newPassword = newPasswordInput.value;

        if (newPassword.length === 0) {
            clearMessage('newPassword-message');
            isNewPasswordValid = false;
            updateSubmitButton();
            return;
        }

        // 1. 길이 검증
        if (newPassword.length < 8 || newPassword.length > 20) {
            showError('newPassword-message', "비밀번호는 8자 이상 20자 이하여야합니다.");
            isNewPasswordValid = false;
            updateSubmitButton();
            return;
        }

        // 2. 숫자 포함 검증
        if (!/\d/.test(newPassword)) {
            showError('newPassword-message', "비밀번호에는 숫자가 하나이상 포함되어야 합니다.");
            isNewPasswordValid = false;
            updateSubmitButton();
            return;
        }

        // 3. 현재 비밀번호와 다른지 검증
        if (currentPassword.length > 0 && currentPassword === newPassword) {
            showError('newPassword-message', "새 비밀번호는 현재 비밀번호와 달라야 합니다.");
            isNewPasswordValid = false;
            updateSubmitButton();
            return;
        }

        // 모든 검증 통과
        showSuccess('newPassword-message', "사용 가능한 비밀번호입니다.");
        isNewPasswordValid = true;
        updateSubmitButton();
    }

    // 비밀번호 확인 검증 로직
    newPasswordInput.addEventListener('input', checkPasswordMatch)
    newPasswordConfirmInput.addEventListener('input', checkPasswordMatch)

    // form 제출 전 최종 검증 기능
    signForm.addEventListener('submit', function(event) {
        if (!isCurrentPasswordValid || !isNewPasswordValid || !isNewPasswordMatchValid) {

            event.preventDefault();

            alert("모든 항목을 올바르게 입력하세요");

            if (!isCurrentPasswordValid) {
                showError('currentPassword-message', "현재 비밀번호를 확인하세요");
            }
            if (!isNewPasswordValid) {
                showError('newPassword-message', "새 비밀번호를 확인하세요");
            }
            if (!isNewPasswordMatchValid) {
                showError('newPasswordConfirm-message', "새 비밀번호 확인을 확인하세요");
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
        const newPassword = newPasswordInput.value;
        const newPasswordConfirm = newPasswordConfirmInput.value;
        if (newPasswordConfirm === '') {
            // 아직 입력 없음
            isNewPasswordMatchValid = false;
            updateSubmitButton();
        } else if (newPassword === newPasswordConfirm) {
            showSuccess('newPasswordConfirm-message', "비밀번호가 일치합니다.");
            isNewPasswordMatchValid = true;
            updateSubmitButton();
        } else {
            showError('newPasswordConfirm-message', "비밀번호가 일치하지 않습니다.");
            isNewPasswordMatchValid = false;
            updateSubmitButton();
        }
    }

    // 제출 버튼 비활성화 기능
    function updateSubmitButton() {
        const submitButton = document.querySelector('button[type="submit"]');

        if (isCurrentPasswordValid && isNewPasswordValid && isNewPasswordMatchValid) {

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