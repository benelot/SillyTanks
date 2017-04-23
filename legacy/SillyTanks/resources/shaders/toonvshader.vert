varying vec3 lightDir,normal;

void main()
{
	lightDir = normalize(vec3(gl_LightSource[0].position));
	normal = gl_NormalMatrix * gl_Normal;
	
	// Textures coordinates
	gl_TexCoord[0] = gl_MultiTexCoord0;

	gl_Position = ftransform();
} 
