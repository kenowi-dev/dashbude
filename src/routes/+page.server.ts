import type { PageServerLoad } from "./$types"
import {dashboard} from "$lib/server/dashboard-watcher";



export const load: PageServerLoad = () => {
    return {
        dashboard: dashboard
    }

}