import { useTranslation } from 'react-i18next';
import DataObject from '../../../lib/domain/entities/DataObject';
import { useValidation } from '../../common/useValidation';

export const useDataObjectImportSchema = () => {
    const { required, sizeLimit } = useValidation();
    const { i18n } = useTranslation(['dataObject']);

    const fields = {
        selectedSystem: {
            label: i18n.t('dataObject:selectedSystem'),
            name: 'selectedSystem'
        },
        selectedFileType: {
            label: i18n.t('dataObject:selectedFileType'),
            name: 'selectedFileType'
        },
        selectedFiles: {
            label: i18n.t('dataObject:selectedFiles'),
            name: 'selectedFiles'
        },
        dataObjectList: {
            label: i18n.t('dataObject:file'),
            name: 'dataObjectList'
        },
        identifier: {
            label: i18n.t('dataObject:identifier'),
            name: 'identifier',
            minSize: 6,
            maxSize: 8,
            regExp: RegExp(/^[a-zA-Z][a-zA-Z0-9-]*$/),
            reservedWords: ['ACCEPT', 'ACCESS', 'ADD', 'ADVANCING', 'AFTER', 'ALL', 'ALPHABETIC', 'ALSO', 'ALTER',
                'ALTERNATE', 'ARE', 'AREA', 'AREAS', 'ASCENDING', 'ASSIGN', 'AT', 'AUTHOR', 'BEFORE', 'BLANK', 'BLOCK', 'BOTTOM', 'BY', 'CALL', 'CANCEL',
                'CD', 'CF', 'CH', 'CHARACTER', 'CHARACTERS', 'CLOCK-UNITS', 'CLOSE', 'COBOL', 'CODE', 'CODE-SET', 'COLLATING', 'COLUMN', 'COMMA', 'COMMUNICATION',
                'COMP', 'COMP-0', 'COMPUTATIONAL-0', 'COMPUTE', 'CONFIGURATION', 'CONTAINS', 'CONTROL', 'CONTROL-AREA', 'COPY', 'CORR', 'CORRESPONDING',
                'COUNT', 'CURRENCY', 'DATA', 'DATE', 'DATE-COMPILED', 'DATE-WRITTEN', 'DAY', 'DE', 'DEBUG-CONTENTS', 'DEBUG-ITEM', 'DEBUG-LINE', 'DEBUG-NAME',
                'DEBUG-SUB-1', 'DEBUG-SUB-2', 'DEBUG-SUB-3', 'DEBUGGING', 'DECIMAL-POINT', 'DECLARATIVES', 'DELETE', 'DELIMITED', 'DELIMITER', 'DEPENDING',
                'DESCENDING', 'DESTINATION', 'DETAIL', 'DISABLE', 'DISPLAY', 'DIVIDE', 'DIVISION', 'DOWN', 'DUPLICATES', 'DYNAMIC', 'EGI', 'ELSE', 'EMI', 'ENABLE',
                'END', 'END-OF-PAGE', 'ENTER', 'ENVIRONMENT', 'EOP', 'ERROR', 'ESI', 'EVERY', 'EXCEPTION', 'EXEC', 'EXIT', 'EXTEND', 'FD', 'FILE', 'FILE-CONTROL',
                'FILLER', 'FINAL', 'FIRST', 'FOOTING', 'FOR', 'FROM', 'GENERATE', 'GIVING', 'GO', 'GROUP', 'HEADING', 'I-O', 'I-O-CONTROL', 'IDENTIFICATION', 'IF',
                'IN', 'INDEX', 'INDEXED', 'INDICATE', 'INITIAL', 'INITIATE', 'INPUT', 'INPUT-OUTPUT', 'INSPECT', 'INSTALLATION', 'INTO', 'INVALID', 'IS', 'JUST,JUSTIFIED',
                'KEY', 'LABEL', 'LAST', 'LEADING', 'LEFT', 'LENGTHLIMIT', 'LIMITS', 'LINAGE', 'LINAGE-COUNTER', 'LINE', 'LINE-COUNTER', 'LINES', 'LINKAGE', 'LOCK',
                'MEMORY', 'MERGE', 'MESSAGE', 'MODE', 'MOVE', 'MODULES', 'MORE-LABELS', 'MULTIPLE', 'MULTIPLY', 'NATIVE', 'NEGATIVE', 'NEXT', 'NO', 'NUMBER', 'NUMERIC',
                'OBJECT-COMPUTER', 'OCCURS', 'OF', 'OFF', 'OMITTED', 'ON', 'OPEN', 'OPTIONAL', 'ORGANIZATION', 'OUTPUT', 'OVERFLOW', 'PAGE', 'PAGE-COUNTER', 'PERFORM', 'PF', 'PH', 'PIC', 'PICTURE',
                'PLUS', 'POINTER', 'POSITION', 'POSITIVE', 'PRINTING', 'PROCEDURE', 'PROCEDURES', 'PROCEED', 'PROGRAM', 'PROGRAM-ID', 'QUEUE', 'QUOTE', 'QUOTES', 'RANDOM',
                'RD', 'READ', 'RECEIVE', 'RECORD', 'RECORDS', 'REDEFINES', 'REEL', 'REFERENCES', 'RELATIVE', 'RELEASE', 'REMAINDER', 'REMOVAL', 'RENAMES', 'REPLACING',
                'REPORT', 'REPORTING', 'REPORTS', 'RERUN', 'RESERVE', 'RETURN', 'REVERSED', 'REWIND', 'REWRITE', 'RF', 'RH', 'RIGHT', 'ROUNDED', 'RUN', 'SAME', 'SD', 'SEARCH',
                'SECTION', 'SECURITY', 'SEGMENT', 'SEGMENT-LIMIT', 'SELECT', 'SEND', 'SENTENCE', 'SEPARATE', 'SEQUENCE', 'SEQUENTIAL', 'SET', 'SIGN', 'SIZE', 'SORT', 'SORT-MERGE',
                'SOURCE', 'SOURCE-COMPUTER', 'SPECIAL-NAMES', 'STANDARD,STANDARD-1', 'START', 'STATUS', 'STOP', 'STRING', 'SUB-QUEUE-1', 'SUB-QUEUE-2', 'SUB-QUEUE-3', 'SUBTRACT',
                'SUM', 'SUPER', 'SYMBOLIC', 'SYNC', 'SYNCHRONIZED', 'TALLYING', 'TAPE', 'TERMINAL', 'TERMINATE', 'TEXT', 'THROUGH', 'THRU', 'TIME', 'TIMES', 'TO', 'TOP', 'TRAILING',
                'TYPE', 'UNIT', 'UNSTRING', 'UNTIL', 'UP', 'UPON', 'USAGE', 'USE', 'USING', 'VALUE', 'VALUES', 'VARYING', 'WHEN', 'WITH', 'WORDS', 'WORKING-STORAGE', 'WRITE,ALPHABET',
                'ALPHABETIC-LOWER', 'ALPHABETIC-UPPER', 'ALPHANUMERIC', 'ALPHANUMERIC-EDITED', 'ANY', 'BINARY', 'CLASS', 'COMMON', 'CONTENT', 'CONTINUE', 'CONVERTING', 'DAY-OF-WEEK',
                'END-ADD', 'END-CALL', 'END-COMPUTE', 'END-DELETE', 'END-DIVIDE', 'END-EVALUATE', 'END-EXEC', 'END-IF', 'END-MULTIPLY', 'END-PERFORM', 'END-READ', 'END-RECEIVE',
                'END-RETURN', 'END-REWRITE', 'END-SEARCH', 'END-START', 'END-STRING', 'END-SUBTRACT', 'END-UNSTRING', 'END-WRITE', 'EVALUATE,EXTERNAL', 'FUNCTION', 'GLOBAL',
                'INITIALIZE', 'NUMERIC-EDITED', 'ORDER', 'OTHER', 'PACKED-DECIMAL', 'PADDING', 'PURGE', 'REFERENCE', 'REPLACE,STANDARD-2', 'TEST', 'THEN', 'END-EXEC', 'CHAIN,END-CHAIN',
                'ENTRY', 'EXAMINE', 'INVOKE', 'NOTE', 'ON', 'ROLLBACK', 'TRANSFORM', 'END-OF-PAGE', 'TOP-OF-PAGE', 'EOP', 'GOBACK']
        },
        description: {
            label: i18n.t('dataObject:description'),
            name: 'description',
            minSize: 1,
            maxSize: 40
        }
    };

    const validateDataObjectList = (value: DataObject[]): string => {
        return (value && value.length === 0) ? i18n.t('message:validation:required', { what: fields.dataObjectList.label }) : '';
    };

    const validateIdentifiers = (value: Map<string, string>): Map<string, string> | null => {
        const errors: Map<string, string> = new Map();
        value.forEach((v: string, k: string) => {
            const error = validateIdentifier(v);
            if (error) {
                errors.set(k, error);
            }
        });
        return errors.size !== 0 ? errors : null;
    };

    const validateIdentifier = (value: string): string =>
        required(fields.identifier.label, value) ||
        (fields.identifier.regExp.test(value) ? '' : i18n.t('message:validation:pattern', { what: fields.identifier.label })) ||
        ((fields.identifier.reservedWords.indexOf(value.trim()) < 0) ? '' : i18n.t('dataObject:message:identifierCannotBeCobolReservedWord')) ||
        sizeLimit(fields.identifier.label, value, fields.identifier.minSize, fields.identifier.maxSize);

    const validateDescriptions = (value: Map<string, string>): Map<string, string> | null => {
        const errors: Map<string, string> = new Map();
        value.forEach((v: string, k: string) => {
            const error = validateDescription(v);
            if (error) {
                errors.set(k, error);
            }
        });
        return errors.size !== 0 ? errors : null;
    };

    const validateDescription = (value: string): string =>
        required(fields.description.label, value) ||
        sizeLimit(fields.description.label, value, fields.description.minSize, fields.description.maxSize);

    const validations = {
        [fields.dataObjectList.name]: validateDataObjectList,
        [fields.identifier.name]: validateIdentifiers,
        [fields.description.name]: validateDescriptions
    };

    return { fields, validations };
};