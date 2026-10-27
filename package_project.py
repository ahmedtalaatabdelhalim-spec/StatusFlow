import zipfile
import os

def zip_folder(folder_path, output_path):
    with zipfile.ZipFile(output_path, 'w', zipfile.ZIP_DEFLATED) as zipf:
        for root, dirs, files in os.walk(folder_path):
            for file in files:
                file_path = os.path.join(root, file)
                # Skip the zip file itself if it's in the same folder
                if file == os.path.basename(output_path):
                    continue
                # Create archive path relative to the folder_path
                arc_name = os.path.relpath(file_path, folder_path)
                zipf.write(file_path, arc_name)

if __name__ == "__main__":
    folder = "C:\\Users\\Ahmed Talaat\\AppData\\Local\\Claude-3p\\local-agent-mode-sessions\\d5cf2dcd\\00000000\\0abc81d1\\outputs"
    output = os.path.join(folder, "StatusFlow_Source.zip")
    try:
        zip_folder(folder, output)
        print(f"Successfully created {output}")
    except Exception as e:
        print(f"Error: {str(e)}")
