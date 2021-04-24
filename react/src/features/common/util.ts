const DEFAULT_TEXTAREA_ROWS = 3;

export const getTextAreaRows = (value: string): number => {
    const TEXTAREA_WIDTH = 35;
    if (!value || value.length === 0) {
        return DEFAULT_TEXTAREA_ROWS;
    }
    const lineBreaks = value.split(/\r\n|\r|\n/).length;
    const linesByLength = Math.ceil(value.length / TEXTAREA_WIDTH);
    return Math.max(DEFAULT_TEXTAREA_ROWS, lineBreaks + linesByLength + 2);
};
