package constants

import androidx.compose.ui.graphics.Color

class MKReportsConstants private constructor() {
    object APP {
        const val APP_NAME = "MKReports"
        const val LOADING_LABEL = "A carregar..."
        object COLORS{
            val MARYKAY_PINK = Color(0xFFD497BE)
        }
    }

    object REPORTS{
        const val LADDER_OF_SUCCESS = "Escada de Sucesso"
    }

    object BUTTONS{
        const val LADDER_OF_SUCESS_REPORT = "Escada de Sucesso"
        const val ABOUT = "Sobre"
        const val UPDATE = "Atualizar"
        const val EXPORT_DATA = "Exportar"
    }

    object TEXT_FIELDS{
        const val CONSULTANT_NUMBER = "Consultora"
    }

    object TABLE{
        object ESCADA_SUCESSO {
                const val TABLE_NAME = "TB_EscadaSucesso"
            object COLUMNS{
                const val EscadaSucesso_NumConsultora = "Número"
                const val EscadaSucesso_NomeConsultora = "Nome"
                const val EscadaSucesso_NivelCarreira = "Nível"
                const val EscadaSucesso_TotalNovasConsultoras = "Novas Consultoras"
                const val EscadaSucesso_TotalNovasConsultorasQualificadas = "Consultoras Qualificadas"
                const val EscadaSucesso_TotalPrograma = "Total Programa"
                const val EscadaSucesso_NivelConseguido = "Nível Conseguido"
                const val EscadaSucesso_PrecisaParaNivelSeguinte = "Próximo nível"
                const val EscadaSucesso_Trimestre = "Trimestre"
                const val EscadaSucesso_DataExtracao = "DataExtracao"
            }
        }
    }
}