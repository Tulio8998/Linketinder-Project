export function escapeHtml(value: unknown): string {
    const text = typeof value === 'string'
        ? value
        : value && typeof value === 'object' && 'valor' in value
            ? String(value.valor ?? '')
            : value == null
                ? ''
                : String(value)

    return text.replace(/[&<>"']/g, (character) => ({
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#39;'
    })[character]!)
}
