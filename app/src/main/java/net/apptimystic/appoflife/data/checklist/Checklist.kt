package net.apptimystic.appoflife.data.checklist

import net.apptimystic.appoflife.data.task.Task


class Checklist(private val delegate: List<Task> = listOf(), val name: String = "noname" ) : List<Task> by delegate
class MutableChecklist(private val delegate: MutableList<Task> = mutableListOf(), val name: String = "noname" ) : MutableList<Task> by delegate