import readline


class OctopusShellCompleter(object):
    def __init__(self, shell):
        self.shell = shell
        self.context = None
        self.matches = None
        readline.set_completer_delims(".")

    def complete(self, text, state):
        if state == 0:
            self.set_context()
            if self.context == 'traversal':
                self.matches = self._get_matches(text)
            elif self.context == 'start':
                self.matches = [x for x in ["g", "quit", "graph"] if x.startswith(text)]
            else:
                self.matches = []

        try:
            return self.matches[state]
        except IndexError:
            return None

    def _get_matches(self, text):
        buffer = readline.get_line_buffer()
        tail = buffer.rsplit(".", 1)[0]
        matches = []
        if tail:
            obj_class = self.shell.run_command("{}.getClass()".format(tail))[0].split(" ")[-1]
            obj_classname = obj_class.split(".")[-1]
            if obj_classname == "SugarLoader$GraphTraversalCategory":
                matches += self.shell.run_command("GraphTraversal.class.methods.name.unique()")
                matches += self.shell.run_command("GraphTraversal.metaClass.methods.name.unique()")
                matches += self.shell.run_command("getBinding().getVariable(\"sessionSteps\").keySet()")
            else:
                matches += self.shell.run_command("{}.getMethods().name.unique()".format(obj_class))
        return [match for match in matches if match.startswith(text)]

    def set_context(self):
        line = readline.get_line_buffer()
        self.context = "start"
        for c in reversed(line):
            if c in '.':
                self.context = "traversal"
                return
            if c in ')}':
                self.context = "complete"
                return
            elif c in '({':
                self.context = "groovy"
                return
