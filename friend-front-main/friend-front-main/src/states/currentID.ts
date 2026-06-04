import {ref, watch} from "vue";

function loadCurrentID(): number | undefined {
    const stored = localStorage.getItem('currentUserId');
    return stored ? Number(stored) : undefined;
}

const currentID = ref<number | undefined>(loadCurrentID());

watch(currentID, (val) => {
    if (val !== undefined && val !== null) {
        localStorage.setItem('currentUserId', String(val));
    } else {
        localStorage.removeItem('currentUserId');
    }
});

export { currentID };
