import subprocess

def run_sorter_jar(*args):
    command = ["java", "--enable-preview", "-jar", "./Sorter.jar"] + list(args)
    subprocess.run(command)

if __name__ == "__main__":
    run_sorter_jar("1", "2", "3")
