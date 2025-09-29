<script>
    /**
    * 공통 JavaScript 함수들
    */

    // 전역 유틸리티 객체
    const EventPlatformUtils = {

    /**
     * 클립보드에 텍스트 복사
     */
    copyToClipboard: function(text) {
    if (navigator.clipboard) {
    return navigator.clipboard.writeText(text).then(() => {
    this.showToast('복사완료', `${text}가 복사되었습니다.`, 'success');
    return true;
}).catch(() => {
    this.fallbackCopyToClipboard(text);
    return false;
});
} else {
    this.fallbackCopyToClipboard(text);
}
},

    /**
     * Clipboard API를 지원하지 않는 브라우저용 fallback
     */
    fallbackCopyToClipboard: function(text) {
    const textArea = document.createElement('textarea');
    textArea.value = text;
    textArea.style.position = 'fixed';
    textArea.style.left = '-999999px';
    textArea.style.top = '-999999px';
    document.body.appendChild(textArea);
    textArea.focus();
    textArea.select();

    try {
    document.execCommand('copy');
    this.showToast('복사완료', `${text}가 복사되었습니다.`, 'success');
} catch (err) {
    this.showToast('복사실패', '복사에 실패했습니다.', 'error');
}

    document.body.removeChild(textArea);
},

    /**
     * 토스트 알림 표시
     */
    showToast: function(title, message, type = 'info') {
    const toastContainer = this.getToastContainer();
    const toastId = 'toast_' + Date.now();
    const bgClass = type === 'success' ? 'bg-success' :
    type === 'error' ? 'bg-danger' :
    type === 'warning' ? 'bg-warning' : 'bg-info';

    const toastHtml = `
            <div class="toast ${bgClass} text-white" role="alert" id="${toastId}">
                <div class="toast-header">
                    <i class="fas fa-${this.getToastIcon(type)} me-2"></i>
                    <strong class="me-auto">${title}</strong>
                    <button type="button" class="btn-close" data-bs-dismiss="toast"></button>
                </div>
                <div class="toast-body">
                    ${message}
                </div>
            </div>
        `;

    toastContainer.insertAdjacentHTML('beforeend', toastHtml);

    const toastElement = document.getElementById(toastId);
    const toast = new bootstrap.Toast(toastElement, { delay: 3000 });
    toast.show();

    // 자동 제거
    setTimeout(() => {
    if (toastElement.parentNode) {
    toastElement.parentNode.removeChild(toastElement);
}
}, 4000);
},

    /**
     * 토스트 컨테이너 가져오기/생성
     */
    getToastContainer: function() {
    let container = document.getElementById('toast-container');
    if (!container) {
    container = document.createElement('div');
    container.id = 'toast-container';
    container.className = 'toast-container position-fixed top-0 end-0 p-3';
    container.style.zIndex = '9999';
    document.body.appendChild(container);
}
    return container;
},

    /**
     * 토스트 아이콘 매핑
     */
    getToastIcon: function(type) {
    const iconMap = {
    'success': 'check-circle',
    'error': 'exclamation-triangle',
    'warning': 'exclamation-circle',
    'info': 'info-circle'
};
    return iconMap[type] || 'info-circle';
},

    /**
     * 숫자를 한국 통화 형식으로 포맷팅
     */
    formatCurrency: function(amount) {
    return new Intl.NumberFormat('ko-KR').format(amount) + '원';
},

    /**
     * 전화번호 자동 포맷팅
     */
    formatPhoneNumber: function(phoneNumber) {
    const cleaned = phoneNumber.replace(/\D/g, '');
    const match = cleaned.match(/^(\d{3})(\d{3,4})(\d{4})$/);

    if (match) {
    return `${match[1]}-${match[2]}-${match[3]}`;
}

    return phoneNumber;
},

    /**
     * 날짜 포맷팅
     */
    formatDate: function(dateString, format = 'YYYY.MM.DD HH:mm') {
    const date = new Date(dateString);

    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hour = String(date.getHours()).padStart(2, '0');
    const minute = String(date.getMinutes()).padStart(2, '0');

    return format
    .replace('YYYY', year)
    .replace('MM', month)
    .replace('DD', day)
    .replace('HH', hour)
    .replace('mm', minute);
},

    /**
     * 로딩 상태 표시/숨김
     */
    showLoading: function(element, text = '처리중...') {
    if (typeof element === 'string') {
    element = document.querySelector(element);
}

    if (element) {
    element.disabled = true;
    element.innerHTML = `<span class="loading-spinner me-2"></span>${text}`;
}
},

    hideLoading: function(element, originalText) {
    if (typeof element === 'string') {
    element = document.querySelector(element);
}

    if (element) {
    element.disabled = false;
    element.innerHTML = originalText;
}
},

    /**
     * AJAX 요청 헬퍼
     */
    ajaxRequest: function(url, options = {}) {
    const defaultOptions = {
    method: 'GET',
    headers: {
    'Content-Type': 'application/json',
}
};

    const finalOptions = Object.assign(defaultOptions, options);

    return fetch(url, finalOptions)
    .then(response => {
    if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`);
}
    return response.json();
})
    .catch(error => {
    console.error('AJAX request failed:', error);
    this.showToast('오류', '요청 처리 중 오류가 발생했습니다.', 'error');
    throw error;
});
},

    /**
     * 폼 유효성 검사
     */
    validateForm: function(formElement) {
    const requiredFields = formElement.querySelectorAll('[required]');
    let isValid = true;

    requiredFields.forEach(field => {
    if (!field.value.trim()) {
    this.showFieldError(field, '필수 입력 항목입니다.');
    isValid = false;
} else {
    this.hideFieldError(field);
}
});

    return isValid;
},

    /**
     * 필드 에러 표시
     */
    showFieldError: function(field, message) {
    field.classList.add('is-invalid');

    let errorDiv = field.parentNode.querySelector('.invalid-feedback');
    if (!errorDiv) {
    errorDiv = document.createElement('div');
    errorDiv.className = 'invalid-feedback';
    field.parentNode.appendChild(errorDiv);
}
    errorDiv.textContent = message;
},

    /**
     * 필드 에러 숨김
     */
    hideFieldError: function(field) {
    field.classList.remove('is-invalid');

    const errorDiv = field.parentNode.querySelector('.invalid-feedback');
    if (errorDiv) {
    errorDiv.remove();
}
},

    /**
     * 모달 표시
     */
    showModal: function(title, content, size = 'md') {
    const modalId = 'dynamic-modal-' + Date.now();
    const modalHtml = `
            <div class="modal fade" id="${modalId}" tabindex="-1">
                <div class="modal-dialog modal-${size}">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">${title}</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            ${content}
                        </div>
                    </div>
                </div>
            </div>
        `;

    document.body.insertAdjacentHTML('beforeend', modalHtml);

    const modalElement = document.getElementById(modalId);
    const modal = new bootstrap.Modal(modalElement);
    modal.show();

    // 모달이 닫힐 때 DOM에서 제거
    modalElement.addEventListener('hidden.bs.modal', function() {
    modalElement.remove();
});

    return modal;
}
};

    // 전역 함수로 등록 (하위 호환성)
    window.copyToClipboard = EventPlatformUtils.copyToClipboard.bind(EventPlatformUtils);
    window.showToast = EventPlatformUtils.showToast.bind(EventPlatformUtils);

    // 페이지 로드 완료 시 초기화
    document.addEventListener('DOMContentLoaded', function() {
    // 모든 전화번호 입력 필드에 자동 포맷팅 적용
    document.querySelectorAll('input[type="tel"]').forEach(input => {
        input.addEventListener('input', function() {
            this.value = EventPlatformUtils.formatPhoneNumber(this.value);
        });
    });

    // 모든 숫자 입력 필드에 천단위 콤마 적용
    document.querySelectorAll('input[data-format="currency"]').forEach(input => {
    input.addEventListener('input', function() {
    const value = this.value.replace(/[^\d]/g, '');
    if (value) {
    this.value = parseInt(value).toLocaleString();
}
});
});

    // 모든 폼에 기본 유효성 검사 적용
    document.querySelectorAll('form').forEach(form => {
    form.addEventListener('submit', function(e) {
    if (!EventPlatformUtils.validateForm(this)) {
    e.preventDefault();
    EventPlatformUtils.showToast('입력 확인', '필수 입력 항목을 모두 입력해주세요.', 'warning');
}
});
});

    // 페이지 애니메이션 적용
    document.querySelectorAll('.card, .alert').forEach((element, index) => {
    element.style.animationDelay = `${index * 0.1}s`;
    element.classList.add('fade-in');
});
});

    // 페이지 언로드 시 정리 작업
    window.addEventListener('beforeunload', function() {
    // 진행 중인 요청이 있으면 사용자에게 확인
    // (실제로는 필요한 경우에만 사용)
});
</script>
